package radius.web.service;

import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import radius.data.repository.JDBCNewsletterRepository;
import radius.data.repository.NewsletterRepository;
import radius.web.components.EmailService;
import radius.web.components.ProfileDependentProperties;

import java.util.Locale;

@Service
public class NewsletterService {

    private NewsletterRepository newsletterRepository;
    private MessageSource messageSource;
    private ProfileDependentProperties prop;
    private EmailService emailService;
    private JavaMailSenderImpl newsletterMailSender;

    private static final String NEWSLETTER_EMAIL_SUBJECT = "email.newsletter.subscribe.title";
    private static final String NEWSLETTER_EMAIL_MESSAGE = "email.newsletter.subscribe.content";
    private static final String NEWSLETTER_EMAIL_FOOTER = "email.newsletter.footer";

    public NewsletterService(JDBCNewsletterRepository newsletterRepository, MessageSource messageSource,
                             ProfileDependentProperties prop, EmailService emailService,
                             JavaMailSenderImpl newsletterMailSender) {
        this.newsletterRepository = newsletterRepository;
        this.messageSource = messageSource;
        this.prop = prop;
        this.emailService = emailService;
        this.newsletterMailSender = newsletterMailSender;
    }

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
        content += "\n\n-----------\n";
        content += messageSource.getMessage(NEWSLETTER_EMAIL_FOOTER,
                new Object[]{prop.getUrl() + "/unsubscribe?uuid=" + uuid}, locale);
        try {
            emailService.sendSimpleMessage(email, subject, content, newsletterMailSender);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
