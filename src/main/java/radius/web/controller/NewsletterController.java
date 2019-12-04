package radius.web.controller;

import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import radius.data.dto.EmailDto;
import radius.data.repository.JDBCNewsletterRepository;
import radius.web.components.EmailService;
import radius.web.components.ModelRepository;
import radius.web.components.ProfileDependentProperties;

import javax.validation.Valid;
import java.util.Locale;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class NewsletterController {

    private MessageSource messageSource;
    private JDBCNewsletterRepository newsletterRepo;
    private JavaMailSenderImpl newsletterMailSender;
    private EmailService emailService;
    private ProfileDependentProperties prop;
    private ModelRepository modelRepository;

    private static final String NEWSLETTER_EMAIL_SUBJECT = "email.newsletter.subscribe.title";
    private static final String NEWSLETTER_EMAIL_MESSAGE = "email.newsletter.subscribe.content";
    private static final String NEWSLETTER_EMAIL_FOOTER = "email.newsletter.footer";
    private static final String REGISTRATION_WEBSITE = "Website";

    public NewsletterController(MessageSource messageSource, JDBCNewsletterRepository newsletterRepo,
                                JavaMailSenderImpl newsletterMailSender, EmailService emailService,
                                ProfileDependentProperties prop, ModelRepository modelRepository) {
         this.messageSource = messageSource;
         this.newsletterRepo = newsletterRepo;
         this.newsletterMailSender = newsletterMailSender;
         this.emailService = emailService;
         this.prop = prop;
         this.modelRepository = modelRepository;
    }

    @RequestMapping(path="/subscribe", method=GET)
    public String getSubscribe(Model model) {
        model.addAllAttributes(modelRepository.homeAttributes());
        return "home";
    }

    @RequestMapping(path="/subscribe", method=POST)
    public String subscribe(@ModelAttribute("subscriptionForm") @Valid EmailDto subscriptionForm,
                            BindingResult result, Model model, Locale loc) {
        if(result.hasErrors()) {
            model.addAttribute("generic_error", Boolean.TRUE);
            model.addAllAttributes(modelRepository.homeAttributes());
            return "home";
        }
        try {
            return cleanlySubscribeToNewsletter(model, subscriptionForm.getEmail(), loc);
        } catch (Exception e) {
            model.addAttribute("generic_error", Boolean.TRUE);
            model.addAllAttributes(modelRepository.homeAttributes());
            return "home";
        }
    }

    @RequestMapping(path="/unsubscribe", method=GET)
    public String unsubscribe(@RequestParam(value = "uuid", required = true) String uuid, Model model) {
        try {
            newsletterRepo.unsubscribe(uuid);
        } catch (Exception e) {
            model.addAttribute("generic_error", Boolean.TRUE);
            model.addAllAttributes(modelRepository.homeAttributes());
            return "home";
        }

        model.addAttribute("newsletter_unsubscribe_success", Boolean.TRUE);
        model.addAllAttributes(modelRepository.homeAttributes());
        return "home";
    }

    private String cleanlySubscribeToNewsletter(Model model, String email, Locale locale) {
        if(!newsletterRepo.alreadySubscribed(email)) {
            String uuid = newsletterRepo.subscribe(email, REGISTRATION_WEBSITE);
            sendSubscriptionEmail(email, uuid, locale);
        }
        model.addAttribute("newsletter_subscribe_success", Boolean.TRUE);
        model.addAllAttributes(modelRepository.homeAttributes());
        return "home";
    }

    private void sendSubscriptionEmail(String email, String uuid, Locale locale) {
        String subject = messageSource.getMessage(NEWSLETTER_EMAIL_SUBJECT, new Object[]{}, locale);
        String content = messageSource.getMessage(NEWSLETTER_EMAIL_MESSAGE, new Object[]{}, locale);
        content += "\n\n-----------\n";
        content += messageSource.getMessage(NEWSLETTER_EMAIL_FOOTER,
                new Object[]{prop.getUrl() + "/unsubscribe?uuid=" + uuid}, locale);
        emailService.sendSimpleMessage(email, subject, content, newsletterMailSender);
    }
}
