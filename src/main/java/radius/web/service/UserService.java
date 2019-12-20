package radius.web.service;

import org.springframework.context.MessageSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import radius.HalfEdge;
import radius.User;
import radius.UserPair;
import radius.data.form.MeetingFeedbackForm;
import radius.data.form.UserForm;
import radius.data.repository.*;
import radius.exceptions.EmailAlreadyExistsException;
import radius.exceptions.UserHasMatchesException;
import radius.web.components.CountrySpecificProperties;
import radius.web.components.EmailService;
import radius.web.components.ProfileDependentProperties;
import radius.web.components.RealWorldProperties;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private UserRepository userRepo;
    private MatchingRepository matchRepo;
    private CountrySpecificProperties countryProperties;
    private RealWorldProperties realWorld;
    private EmailService emailService;
    private JavaMailSenderImpl helloMailSender;
    private ProfileDependentProperties prop;
    private MessageSource messageSource;
    private PasswordEncoder encoder;

    private static final String EMAIL_CONFIRM_SUBJECT = "email.confirm.title";
    private static final String EMAIL_CONFIRM_MESSAGE = "email.confirm.content";

    public UserService(JDBCUserRepository userRepo, JDBCMatchingRepository matchRepo,
                       CountrySpecificProperties countryProperties, RealWorldProperties realWorld,
                       EmailService emailService, JavaMailSenderImpl helloMailSender, ProfileDependentProperties prop,
                       MessageSource messageSource, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.matchRepo = matchRepo;
        this.countryProperties = countryProperties;
        this.realWorld = realWorld;
        this.emailService = emailService;
        this.helloMailSender = helloMailSender;
        this.prop = prop;
        this.messageSource = messageSource;
        this.encoder = encoder;
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
        if (realWorld.isSpecialIsActive() && user.getSpecialanswers().size() > 0) {
            userRepo.updateVotes(user.getEmail(), realWorld.getCurrentVote(), user.getSpecialanswers());
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
            UserPair up = UserPair.of(user, match);
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
            userRepo.deleteUser(username);
        } catch (UserHasMatchesException matchE) {
            return false;
        }
        return true;
    }

    public List<User> matchableUsers() {
        try {
            return userRepo.matchableUsers();
        } catch (Exception e) {
            return Collections.emptyList();
        }
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
}
