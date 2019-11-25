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
import radius.data.form.EmailDto;
import radius.data.form.passwordUuidDto;
import radius.data.JDBCUserRepository;
import radius.web.components.EmailService;

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

    @Autowired
    public ResetPasswordController(EmailService _ses, JavaMailSenderImpl helloMailSender, MessageSource messageSource,
                                   JDBCUserRepository userRepo) {
        this.emailService = _ses;
        this.helloMailSender = helloMailSender;
        this.messageSource = messageSource;
        this.userRepo = userRepo;
    }

    @RequestMapping(method=GET)
    public String forgot(@RequestParam(value = "uuid", required = false) String uuid, Model model) {
        if(uuid == null) {
            model.addAttribute("emailForm", new EmailDto());
            return "forgot";
        }
        else {
            model.addAttribute("uuid", uuid);
            model.addAttribute("passwordForm", new passwordUuidDto());
            return "reset";
        }
    }

    @RequestMapping(method=POST)
    public String forgotten(@ModelAttribute("emailForm") @Valid EmailDto emailForm, BindingResult result, Model model,
                            Locale locale) {
        if(result.hasErrors()) {
            System.out.println("ResetEmailController: Bad email address");
            return "forgot";
        }
        String email = emailForm.getEmail();
        String uuid = userRepo.findUuidByEmail(email);
        if(uuid != null) {
            System.out.println(messageSource.getMessage("email.forgot.title", new Object[]{}, locale));
            System.out.println(messageSource.getMessage("email.forgot.content",
                    new Object[]{"https://radius-schweiz.ch/forgot?uuid=" + uuid}, locale));
            try {
                emailService.sendSimpleMessage(
                        email,
                        messageSource.getMessage("email.forgot.title", new Object[]{}, locale),
                        messageSource.getMessage("email.forgot.content",
                                new Object[]{"https://radius-schweiz.ch/forgot?uuid=" + uuid}, locale),
                        helloMailSender
                );
                System.out.println("Sent PW recovery email to " + email);
            }
            catch (Exception e) {
                System.out.println("Sending PW recovery email to " + email + " FAILED");
                e.printStackTrace();
            }
        }
        model.addAttribute("sent", Boolean.TRUE);
        return "forgot";
    }
}
