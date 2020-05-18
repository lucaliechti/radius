package radius.web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import radius.User;
import radius.data.dto.EmailSourceDto;
import radius.data.repository.JDBCNewsletterRepository;
import radius.data.repository.JDBCUserRepository;
import radius.data.repository.NewsletterRepository;
import radius.data.repository.UserRepository;
import radius.web.components.EmailService;
import radius.web.components.ProfileDependentProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Service
public class NewsletterService {

    private final NewsletterRepository newsletterRepository;
    private final UserRepository userRepository;
    private final MessageSource messageSource;
    private final ProfileDependentProperties prop;
    private final EmailService emailService;
    private final JavaMailSenderImpl newsletterMailSender;
    private final JavaMailSenderImpl helloMailSender;

    private static final String NEWSLETTER_EMAIL_SUBJECT = "email.newsletter.subscribe.title";
    private static final String NEWSLETTER_EMAIL_MESSAGE = "email.newsletter.subscribe.content";
    private static final String NEWSLETTER_EMAIL_FOOTER = "email.newsletter.footer";
    private static final String NEWSLETTER_EMAIL_DEAR = "admin.newsletter.dear";

    public boolean unsubscribe(String uuid) {
        try {
            newsletterRepository.unsubscribe(uuid);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean subscribe(String email, Locale locale, String registrationWebsite) {
        if(!alreadySubscribed(email)) {
            String uuid = newsletterRepository.subscribe(email, registrationWebsite);
            return sendSubscriptionEmail(email, uuid, locale);
        }
        return true;
    }

    private boolean alreadySubscribed(String email) {
        return newsletterRepository.alreadySubscribed(email);
    }

    private boolean sendSubscriptionEmail(String email, String uuid, Locale locale) {
        String subject = messageSource.getMessage(NEWSLETTER_EMAIL_SUBJECT, new Object[]{}, locale);
        String content = messageSource.getMessage(NEWSLETTER_EMAIL_MESSAGE, new Object[]{}, locale);
        content += unsubscribeFooter(uuid, locale);
        try {
            emailService.sendSimpleMessage(email, subject, content, newsletterMailSender);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public List<EmailSourceDto> allRecipients() {
        return newsletterRepository.allRecipients();
    }

    public List<String> sendMassEmail(boolean isNewsletter, String subject, String message, List<String> recipients,
                                      Locale locale) {
        String originalMessage = message;
        ArrayList<String> failedRecipients = new ArrayList<>();
        JavaMailSenderImpl sender = isNewsletter ? newsletterMailSender : helloMailSender;
        for(String recipient : recipients) {
            if(isNewsletter) {
                message += unsubscribeFooter(findUuidByEmail(recipient), locale);
            } else {
                message = personalGreeting(recipient, locale) + message;
            }
            try {
                emailService.sendSimpleMessage(recipient, subject, message, sender);
            } catch (Exception e) {
                failedRecipients.add(recipient);
            }
            message = originalMessage;
        }
        return failedRecipients;
    }

    private String findUuidByEmail(String email) {
        return newsletterRepository.findUuidByEmail(email);
    }

    private String unsubscribeFooter(String uuid, Locale locale) {
        String footer = "\n\n-----------\n";
        return footer + messageSource.getMessage(NEWSLETTER_EMAIL_FOOTER,
                new Object[]{prop.getUrl() + "/unsubscribe?uuid=" + uuid}, locale);
    }

    private String personalGreeting(String email, Locale locale) {
        String dear = messageSource.getMessage(NEWSLETTER_EMAIL_DEAR, new Object[]{}, locale);
        User user = userRepository.findUserByEmail(email);
        return dear + " " + user.getFirstname() + " " + user.getLastname() + ",\n\n";
    }
}
