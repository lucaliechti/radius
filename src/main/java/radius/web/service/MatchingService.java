package radius.web.service;

import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import radius.HalfEdge;
import radius.User;
import radius.UserPair;
import radius.data.form.MeetingFeedbackForm;
import radius.data.repository.JDBCUserRepository;
import radius.data.repository.MatchingRepository;
import radius.data.repository.UserRepository;
import radius.web.components.CountrySpecificProperties;
import radius.web.components.EmailService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MatchingService {

    private UserRepository userRepo;
    private MatchingRepository matchRepo;
    private MessageSource messageSource;
    private EmailService emailService;
    private JavaMailSenderImpl matchingMailSender;
    private CountrySpecificProperties countryProperties;

    public MatchingService(JDBCUserRepository userRepo, MatchingRepository matchRepo, MessageSource messageSource,
                           EmailService emailService, JavaMailSenderImpl matchingMailSender,
                           CountrySpecificProperties countryProperties) {
        this.userRepo = userRepo;
        this.matchRepo = matchRepo;
        this.messageSource = messageSource;
        this.emailService = emailService;
        this.matchingMailSender = matchingMailSender;
        this.countryProperties = countryProperties;
    }

    public void deactivateOldMatchesFor(String email) {
        matchRepo.deactivateOldMatchesFor(email);
    }

    public void updateMatchesAfterMeeting(User user, MeetingFeedbackForm feedbackForm) {
        if(feedbackForm.isConfirmed()) {
            confirmHalfEdge(user.getEmail());
        } else {
            unconfirmHalfEdge(user.getEmail());
        }
        deactivateOldMatchesFor(user.getEmail());
    }

    private void confirmHalfEdge(String email) {
        matchRepo.confirmHalfEdge(email);
    }

    private void unconfirmHalfEdge(String email) {
        matchRepo.unconfirmHalfEdge(email);
    }

    public List<HalfEdge> allMatches() {
        try {
            return matchRepo.allMatches();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public void match(UserPair userPair) {
        userPair.user1().setStatus(User.UserStatus.MATCHED);
        userPair.user1().setStatus(User.UserStatus.MATCHED);
        userRepo.updateExistingUser(userPair.user1());
        userRepo.updateExistingUser(userPair.user2());
        matchRepo.createMatch(userPair);
    }

    public void emailUserAboutMatch(User user, User match) {
        try {
            UserPair up = UserPair.of(user, match);
            String matchingLanguages = matchingLanguages(up, user);
            emailService.sendSimpleMessage(
                    user.getEmail(),
                    messageSource.getMessage("email.match.title", new Object[]{}, user.preferredLocale()),
                    messageSource.getMessage("email.match.content", new Object[]{user.getFirstname(), user.getLastname(), match.getFirstname(), match.getLastname(), String.join(", ", countryProperties.prettyLocations(new ArrayList<Integer>(up.commonLocations()))), matchingLanguages, match.getEmail()}, user.preferredLocale()),
                    matchingMailSender
            );
        } catch (Exception ignored) { }
    }

    public List<HalfEdge> allMatchesForUser(String email) {
        return matchRepo.allMatchesForUser(email);
    }

    private String matchingLanguages(UserPair up, User user) {
        ArrayList<String> languages = new ArrayList<String>();
        for(String lang : up.commonLanguages()){
            languages.add(messageSource.getMessage("language." + lang, new Object[]{}, user.preferredLocale()));
        }
        return String.join(", ", languages);
    }
}
