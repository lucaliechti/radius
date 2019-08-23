package radius.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import radius.EmailForm;
import radius.NewsletterMessage;
import radius.UserForm;
import radius.UserValidation;
import radius.data.JDBCNewsletterRepository;
import radius.data.StaticResourceRepository;

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
    private JDBCNewsletterRepository newsletterRepo;

    //Only to make i18n work everywhere by providing a GET handler method
    @RequestMapping(path="/subscribe", method=GET)
    public String getsubscribe(Model model, Locale loc) {
        addAttributesTo(model);
        return "home";
    }

    @RequestMapping(path="/subscribe", method=POST)
    public String subscribe(@ModelAttribute("subscriptionForm") @Valid EmailForm subscriptionForm, BindingResult result, Model model, Locale loc) {
        try {
            newsletterRepo.subscribe(subscriptionForm.getEmail(), "Website Subscription");
        }
        catch (Exception e) {
            model.addAttribute("generic_error", Boolean.TRUE);
            addAttributesTo(model);
            return "home";
        }
        model.addAttribute("newsletter_subscribe_success", Boolean.TRUE);
        addAttributesTo(model);
        return "home";
    }

    //We can GET URLs composed like so "https://radius-schweiz.ch/unsubscribe?uuid=" + uuid
    @RequestMapping(path="/unsubscribe", method=GET)
    public String unsubscribe(@RequestParam(value = "uuid", required = true) String uuid, Model model, Locale loc) {
        try {
            newsletterRepo.unsubscribe(uuid);
        }
        catch (Exception e) {
            model.addAttribute("generic_error", Boolean.TRUE);
            addAttributesTo(model);
            return "home";
        }

        model.addAttribute("newsletter_unsubscribe_success", Boolean.TRUE);
        addAttributesTo(model);
        return "home";
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

        List<UserValidation> recipients = newsletterRepo.getRecipients();
        for(UserValidation u : recipients) {
            System.out.println(u.getEmail() + "; " + u.getUuid());
            //SEND EMAIL
            //ADD UNSUBSCRIBE LINK
        }
        model.addAttribute("numberRecipients", newsletterRepo.numberOfRecipients());
        model.addAttribute("send_success", Boolean.TRUE);
        model.addAttribute("newsletterForm", new NewsletterMessage());
        return "admin";
    }

    private void addAttributesTo(Model model) {
        model.addAttribute("registrationForm", new UserForm());
        model.addAttribute("cantons", staticRepo.cantons());
    }

}
