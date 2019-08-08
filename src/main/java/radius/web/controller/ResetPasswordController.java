package radius.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import radius.EmailForm;
import radius.PasswordForm;
import radius.data.UserRepository;
import radius.web.components.EmailService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RequestMapping("/forgot")
@Controller
public class ResetPasswordController {

    @Qualifier("helloMailSender")
    @Autowired
    private JavaMailSenderImpl helloMailSender;

    private EmailService emailService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    public ResetPasswordController(EmailService _ses) {
        this.emailService = _ses;
    }

    @RequestMapping(method=GET)
    public String forgot(@RequestParam(value = "uuid", required = false) String uuid, Model model, HttpServletRequest req, HttpServletResponse res) {
        if(uuid == null) {
            model.addAttribute("emailForm", new EmailForm());
            return "forgot";
        }
        else {
            model.addAttribute("uuid", uuid);
            model.addAttribute("passwordForm", new PasswordForm());
            return "reset";
        }
    }

    @RequestMapping(method=POST)
    public String forgotten(@ModelAttribute("emailForm") @Valid EmailForm emailForm, BindingResult result, Model model, Locale locale) throws UnsupportedEncodingException {
        if(result.hasErrors()) {
            System.out.println("ResetEmailController: Bad email address");
            return "forgot";
        }
        String email = new String(emailForm.getEmail().getBytes("ISO-8859-1"), "UTF-8");

        String uuid = userRepo.findUuidByEmail(email);
        if(uuid != null) {
            System.out.println(messageSource.getMessage("email.forgot.title", new Object[]{}, locale));
            System.out.println(messageSource.getMessage("email.forgot.content", new Object[]{"https://radius-schweiz.ch/forgot?uuid=" + uuid}, locale));
            try {
                emailService.sendSimpleMessage(
                        email,
                        messageSource.getMessage("email.forgot.title", new Object[]{}, locale),
                        messageSource.getMessage("email.forgot.content", new Object[]{"https://radius-schweiz.ch/forgot?uuid=" + uuid}, locale),
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
