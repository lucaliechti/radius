package radius.web.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import radius.HalfEdge;
import radius.User;
import radius.UserPair;
import radius.data.dto.StatisticsDto;
import radius.data.form.MeetingFeedbackForm;
import radius.data.form.UserForm;
import radius.data.repository.*;
import radius.exceptions.EmailAlreadyExistsException;
import radius.exceptions.UserHasMatchesException;
import radius.web.components.CountrySpecificProperties;
import radius.web.components.EmailService;
import radius.web.components.ProfileDependentProperties;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private UserRepository userRepo;
    private MatchingRepository matchRepo;
    private CountrySpecificProperties countryProperties;
    private ConfigService configService;
    private EmailService emailService;
    private JavaMailSenderImpl helloMailSender;
    private ProfileDependentProperties prop;
    private MessageSource messageSource;
    private PasswordEncoder encoder;
    private NewsletterRepository newsletterRepo;

    private static final String EMAIL_CONFIRM_SUBJECT = "email.confirm.title";
    private static final String EMAIL_CONFIRM_MESSAGE = "email.confirm.content";

    public UserService(JDBCUserRepository userRepo, JDBCMatchingRepository matchRepo,
                       CountrySpecificProperties countryProperties, ConfigService configService,
                       EmailService emailService, JavaMailSenderImpl helloMailSender, ProfileDependentProperties prop,
                       MessageSource messageSource, PasswordEncoder encoder, JDBCNewsletterRepository newsletterRepo) {
        this.userRepo = userRepo;
        this.matchRepo = matchRepo;
        this.countryProperties = countryProperties;
        this.configService = configService;
        this.emailService = emailService;
        this.helloMailSender = helloMailSender;
        this.prop = prop;
        this.messageSource = messageSource;
        this.encoder = encoder;
        this.newsletterRepo = newsletterRepo;
    }

    public void activateUser(User user) {
        user.setStatus(User.UserStatus.WAITING);
        updateExistingUser(user);
    }

    public void deactivateUser(User user) {
        user.setStatus(User.UserStatus.INACTIVE);
        updateExistingUser(user);
    }

    public void enableUser(String email) {
        User user = findUserByEmail(email).get();
        user.setEnabled(true);
        user.setUuid(UUID.randomUUID().toString());
        updateExistingUser(user);
    }

    public void updateExistingUser(User user) {
        userRepo.updateExistingUser(user);
        if (configService.specialActive() && user.getSpecialanswers().size() > 0) {
            userRepo.updateVotes(user.getEmail(), configService.currentVote(), user.getSpecialanswers());
        }
    }

    public void saveNewUser(User user) throws EmailAlreadyExistsException {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepo.saveNewUser(user);
    }

    public Optional<User> findUserByEmail(String email) {
        try {
            User user = userRepo.findUserByEmail(email);
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<String> findUuidByEmail(String email) {
        try {
            String uuid = userRepo.findUuidByEmail(email);
            return Optional.of(uuid);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<String> findEmailByUuid(String uuid) {
        try {
            String email = userRepo.findEmailByUuid(uuid);
            return Optional.of(email);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<HalfEdge> allMatchesForUser(String email) {
        return matchRepo.allMatchesForUser(email);
    }

    public boolean updateUserAfterMeeting(User user, MeetingFeedbackForm feedbackForm) {
        switch(feedbackForm.getNextState()) {
            case "WAITING":
                activateUser(user);
                return true;
            case "INACTIVE":
                deactivateUser(user);
                return true;
            default:
                return false;
        }
    }

    public Map<String, Object> userSpecificAttributes(User user) {
        HashMap<String, Object> userSpecificAttributes = new HashMap<>();
        userSpecificAttributes.put("user", user);
        userSpecificAttributes.put("history", allMatchesForUser(user.getEmail()));
        userSpecificAttributes.put("feedbackForm", new MeetingFeedbackForm());
        userSpecificAttributes.put("userlocations", countryProperties.prettyLocations(user.getLocations()));
        if(user.getStatus() == User.UserStatus.MATCHED) {
            User match = matchRepo.getCurrentMatchFor(user.getEmail());
            userSpecificAttributes.put("match", match);
            UserPair up = new UserPair(user, match);
            userSpecificAttributes.put("commonlocations",
                    String.join(", ", countryProperties.prettyLocations(new ArrayList<>(up.commonLocations()))));
            userSpecificAttributes.put("commonlanguages", up.commonLanguages());
        }
        return userSpecificAttributes;
    }

    public boolean registerNewUserFromUserForm(UserForm registrationForm, Locale locale) {
        String firstName = registrationForm.getFirstName();
        String lastName = registrationForm.getLastName();
        String canton = registrationForm.getCanton();
        String email = registrationForm.getEmail();
        String password = registrationForm.getPassword();
        assert canton != null;
        User user = new User(firstName, lastName, canton.equals("NONE") ? null : canton, email, password);
        try {
            saveNewUser(user);
            log.info("Saved new user " + user.getFirstname() + " " + user.getLastname() + " with email " + user.getEmail());
        } catch (EmailAlreadyExistsException e) {
            return false;
        }
        return sendConfirmationEmail(user, locale);
    }

    private boolean sendConfirmationEmail(User user, Locale locale) {
        try {
            emailService.sendSimpleMessage(
                    user.getEmail(),
                    messageSource.getMessage(EMAIL_CONFIRM_SUBJECT, new Object[]{}, locale),
                    messageSource.getMessage(EMAIL_CONFIRM_MESSAGE, new Object[]{user.getFirstname(), user.getLastname(),
                            prop.getUrl() + "/confirm?uuid=" + user.getUuid()}, locale),
                    helloMailSender
            );
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public List<User> allUsers() {
        try {
            return userRepo.allUsers();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public boolean deleteUser(String username) {
        try {
            matchRepo.invalidateMatchesForUser(username);
            userRepo.deleteUser(username);
            log.info("Deleted user " + username);
        } catch (UserHasMatchesException matchE) {
            return false;
        }
        return true;
    }

    public boolean banUser(String username) {
        try {
            userRepo.banUser(username);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean setUserPrivate(String username) {
        try {
            userRepo.setPrivate(username);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void updateLastLogin(String name) {
        userRepo.updateLastLogin(name);
    }

    public int[] regionDensity() {
        int[] registeredUsers = new int[countryProperties.getRegions().keySet().size()];
        List<String> regions = userRepo.regionDensity();
        regions = regions.stream().filter(region -> region != null && region.length() > 0).collect(Collectors.toList());
        regions.forEach( s -> {
            int[] values = Arrays.stream(s.split(";")).mapToInt(Integer::parseInt).toArray();
            Arrays.stream(values).forEach(code -> registeredUsers[code-1]++);
        } );
        return registeredUsers;
    }

    public StatisticsDto getStatistics() {
        Instant now = Instant.now();
        Instant weekAgo = now.minus(7, ChronoUnit.DAYS);
        Instant monthAgo = now.minus(30, ChronoUnit.DAYS);
        StatisticsDto dto = new StatisticsDto();

        List<User> allEnabledUsers = userRepo.allUsers().stream().filter(User::isEnabled).collect(Collectors.toList());
        dto.setUsersActive((int) allEnabledUsers.stream().filter(u -> User.UserStatus.WAITING.equals(u.getStatus())).count());
        dto.setUsersTotal(allEnabledUsers.size());
        dto.setRegistrationsWeek((int) allEnabledUsers.stream().filter(u -> u.getDateCreated().toInstant().compareTo(weekAgo) > 0).count());
        dto.setRegistrationsMonth((int) allEnabledUsers.stream().filter(u -> u.getDateCreated().toInstant().compareTo(monthAgo) > 0).count());
        dto.setOnlineWeek((int) allEnabledUsers.stream().filter(u -> u.getLastLogin() != null && u.getLastLogin().toInstant().compareTo(weekAgo) > 0).count());
        dto.setOnlineMonth((int) allEnabledUsers.stream().filter(u -> u.getLastLogin() != null && u.getLastLogin().toInstant().compareTo(monthAgo) > 0).count());

        List<HalfEdge> matches = matchRepo.allMatches();
        List<HalfEdge> uniqueMatches = matches.stream().filter(m -> m.getEmail1().compareToIgnoreCase(m.getEmail2()) < 0).collect(Collectors.toList());
        dto.setMatchesWeek((int) uniqueMatches.stream().filter(m -> m.getDateCreated().toInstant().compareTo(weekAgo) > 0).count());
        dto.setMatchesMonth((int) uniqueMatches.stream().filter(m -> m.getDateCreated().toInstant().compareTo(monthAgo) > 0).count());
        return dto;
    }

    @Scheduled(cron = "0 0/10 * * * *")
    private void deleteUnconfirmedUsers() {
        Instant tenMinutesAgo = Instant.now().minus(10, ChronoUnit.MINUTES);
        List<User> unconfirmedUsers = userRepo.allUsers().stream().filter(u -> !u.isEnabled()).collect(Collectors.toList());
        List<User> older = unconfirmedUsers.stream().filter(u -> u.getDateCreated().toInstant().compareTo(tenMinutesAgo) < 0).collect(Collectors.toList());
        for(User u : older) {
            deleteUser(u.getEmail());
            try {
                if (newsletterRepo.alreadySubscribed(u.getEmail())) {
                    newsletterRepo.unsubscribe(newsletterRepo.findUuidByEmail(u.getEmail()));
                }
            } catch (Exception ignored) { }
        }
    }
}
