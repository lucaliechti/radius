package radius.web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import radius.User;
import radius.data.repository.UserRepository;
import radius.web.components.EmailService;
import radius.web.components.ProfileDependentProperties;

import java.util.Locale;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PasswordService {

    private final JavaMailSenderImpl helloMailSender;
    private final EmailService emailService;
    private final UserRepository userRepo;
    private final MessageSource messageSource;
    private final ProfileDependentProperties prop;
    private final PasswordEncoder encoder;

    private static final String EMAIL_FORGOT_SUBJECT = "email.forgot.title";
    private static final String EMAIL_FORGOT_MESSAGE = "email.forgot.content";

    public boolean updatePassword(User user, String plaintextPassword) {
        user.setPassword(encoder.encode(plaintextPassword));
        user.setUuid(UUID.randomUUID().toString());
        try {
            userRepo.updateExistingUser(user);
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
