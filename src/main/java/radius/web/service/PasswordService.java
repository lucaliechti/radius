package radius.web.service;

import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import radius.data.repository.JDBCUserRepository;
import radius.data.repository.UserRepository;
import radius.web.components.EmailService;
import radius.web.components.ProfileDependentProperties;

import java.util.Locale;
import java.util.UUID;

@Service
public class PasswordService {

    private JavaMailSenderImpl helloMailSender;
    private EmailService emailService;
    private UserRepository userRepo;
    private MessageSource messageSource;
    private ProfileDependentProperties prop;
    private PasswordEncoder encoder;

    private static final String EMAIL_FORGOT_SUBJECT = "email.forgot.title";
    private static final String EMAIL_FORGOT_MESSAGE = "email.forgot.content";

    public PasswordService(JavaMailSenderImpl helloMailSender, EmailService emailService,
                           JDBCUserRepository userRepo, MessageSource messageSource,
                           ProfileDependentProperties prop, PasswordEncoder encoder) {
        this.helloMailSender = helloMailSender;
        this.emailService = emailService;
        this.userRepo = userRepo;
        this.messageSource = messageSource;
        this.prop = prop;
        this.encoder = encoder;
    }

    public boolean updatePassword(String email, String newPassword) {
        try {
            userRepo.updatePassword(encoder.encode(newPassword), UUID.randomUUID().toString(), email);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void sendPasswordForgottenEmail(String email, String uuid, Locale locale) {
        emailService.sendSimpleMessage(
                email,
                messageSource.getMessage(EMAIL_FORGOT_SUBJECT, new Object[]{}, locale),
                messageSource.getMessage(EMAIL_FORGOT_MESSAGE, new Object[]{prop.getUrl() + "/forgot?uuid=" + uuid},
                        locale),
                helloMailSender
        );
    }
}
