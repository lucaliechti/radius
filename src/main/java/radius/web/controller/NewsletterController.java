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
import radius.NewsletterMessage;
import radius.UserValidation;
import radius.data.JDBCNewsletterRepository;
import radius.data.StaticResourceRepository;
import radius.web.components.EmailService;

import javax.validation.Valid;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class NewsletterController {

    @Autowired
    private StaticResourceRepository staticRepo;

    @Autowired
    private HomeController h;

    @Autowired
    private JDBCNewsletterRepository newsletterRepo;

    @Qualifier("newsletterMailSender")
    @Autowired
    private JavaMailSenderImpl newsletterMailSender;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MessageSource messageSource;

    private final String REGISTRATION_WEBSITE = "Website";

    //Only to make i18n work everywhere by providing a GET handler method
    @RequestMapping(path="/subscribe", method=GET)
    public String getsubscribe(Model model, Locale loc) {
        return h.cleanlyHome(model);
    }

    @RequestMapping(path="/subscribe", method=POST)
    public String subscribe(@ModelAttribute("subscriptionForm") @Valid EmailForm subscriptionForm, BindingResult result, Model model, Locale loc) {
        try {
            return cleanlySubscribeToNewsletter(model, subscriptionForm.getEmail(), REGISTRATION_WEBSITE, loc);
        }
        catch (Exception e) {
            model.addAttribute("generic_error", Boolean.TRUE);
            e.printStackTrace();
            return h.cleanlyHome(model);
        }
    }

    //We can GET URLs composed like so "https://radius-schweiz.ch/unsubscribe?uuid=" + uuid
    @RequestMapping(path="/unsubscribe", method=GET)
    public String unsubscribe(@RequestParam(value = "uuid", required = true) String uuid, Model model, Locale loc) {
        try {
            newsletterRepo.unsubscribe(uuid);
        }
        catch (Exception e) {
            model.addAttribute("generic_error", Boolean.TRUE);
            return h.cleanlyHome(model);
        }

        model.addAttribute("newsletter_unsubscribe_success", Boolean.TRUE);
        return h.cleanlyHome(model);
    }

    @RequestMapping(path="/send", method=POST)
    public String writeNewsletter(@ModelAttribute("newsletterForm") @Valid NewsletterMessage letter, BindingResult result, Model model, Locale loc) throws UnsupportedEncodingException {
        if(result.hasErrors()) {
            model.addAttribute("newsletterForm", letter);
            model.addAttribute("numberRecipients", newsletterRepo.numberOfRecipients());
            return "admin";
        }

        String subject = new String(letter.getSubject().getBytes("ISO-8859-1"), "UTF-8");
        String message = new String(letter.getMessage().getBytes("ISO-8859-1"), "UTF-8");

        message += "\n\n";

        List<UserValidation> recipients = newsletterRepo.getRecipients();
        for(UserValidation u : recipients) {
            System.out.println(u.getEmail() + "; " + u.getUuid());
            //SEND EMAIL
            //ADD UNSUBSCRIBE LINK
            //TODO
        }
        model.addAttribute("numberRecipients", newsletterRepo.numberOfRecipients());
        model.addAttribute("send_success", Boolean.TRUE);
        model.addAttribute("newsletterForm", new NewsletterMessage());
        return "admin";
    }

    public String cleanlySubscribeToNewsletter(Model model, String email, String source, Locale locale) throws Exception {
        if(!newsletterRepo.alreadySubscribed(email)) {
            String uuid = newsletterRepo.subscribe(email, source);

            String subject = messageSource.getMessage("email.newsletter.subscribe.title", new Object[]{}, locale);
            String content = messageSource.getMessage("email.newsletter.subscribe.content", new Object[]{}, locale);
            content += "\n\n-----------\n" + messageSource.getMessage("email.newsletter.footer", new Object[]{"https://radius-schweiz.ch/unsubscribe?uuid=" + uuid}, locale);

            emailService.sendSimpleMessage(email, subject, content, newsletterMailSender);
        }
        model.addAttribute("newsletter_subscribe_success", Boolean.TRUE);
        return h.cleanlyHome(model);
    }
}
