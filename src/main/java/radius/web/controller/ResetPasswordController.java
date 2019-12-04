package radius.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import radius.data.dto.EmailDto;
import radius.data.dto.PasswordUuidDto;
import radius.data.repository.JDBCUserRepository;
import radius.web.components.EmailService;
import radius.web.components.ProfileDependentProperties;

import javax.validation.Valid;
import java.util.Locale;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RequestMapping("/forgot")
@Controller
public class ResetPasswordController {

    private JavaMailSenderImpl helloMailSender;
    private EmailService emailService;
    private MessageSource messageSource;
    private JDBCUserRepository userRepo;
    private ProfileDependentProperties prop;

    private static final String EMAIL_FORGOT_SUBJECT = "email.forgot.title";
    private static final String EMAIL_FORGOT_MESSAGE = "email.forgot.content";

    @Autowired
    public ResetPasswordController(EmailService _ses, JavaMailSenderImpl helloMailSender, MessageSource messageSource,
                                   JDBCUserRepository userRepo, ProfileDependentProperties prop) {
        this.emailService = _ses;
        this.helloMailSender = helloMailSender;
        this.messageSource = messageSource;
        this.userRepo = userRepo;
        this.prop = prop;
    }

    @RequestMapping(method=GET)
    public String forgot(@RequestParam(value = "uuid", required = false) String uuid, Model model) {
        if(uuid == null) {
            model.addAttribute("emailForm", new EmailDto());
            return "forgot";
        } else {
            model.addAttribute("uuid", uuid);
            model.addAttribute("passwordForm", new PasswordUuidDto());
            return "reset";
        }
    }

    @RequestMapping(method=POST)
    public String forgotten(@ModelAttribute("emailForm") @Valid EmailDto emailForm, BindingResult result, Model model,
                            Locale locale) {
        if(result.hasErrors()) {
            return "forgot";
        }
        String email = emailForm.getEmail();
        String uuid = userRepo.findUuidByEmail(email);
        if(uuid != null) {
            try {
                emailService.sendSimpleMessage(
                    email,
                    messageSource.getMessage(EMAIL_FORGOT_SUBJECT, new Object[]{}, locale),
                    messageSource.getMessage(EMAIL_FORGOT_MESSAGE, new Object[]{prop.getUrl() + "/forgot?uuid=" + uuid},
                            locale),
                    helloMailSender
                );
            } catch (Exception e) {
                model.addAttribute("error", Boolean.TRUE);
            }
        }
        model.addAttribute("sentIfExists", Boolean.TRUE);
        return "forgot";
    }
}
