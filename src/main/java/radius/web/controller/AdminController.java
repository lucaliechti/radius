package radius.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import radius.data.form.NewsletterForm;
import radius.web.components.RealWorldProperties;
import radius.web.service.NewsletterService;
import radius.web.service.UserService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class AdminController {

    private NewsletterService newsletterservice;
    private UserService userService;
    private RealWorldProperties realWorld;

    public AdminController(NewsletterService newsletterservice, UserService userService, RealWorldProperties real) {
        this.newsletterservice = newsletterservice;
        this.userService = userService;
        this.realWorld = real;
    }

    @RequestMapping(path="/admin")
    public String admin() {
        return "admin";
    }

    @RequestMapping(path="/contactUsers", method=POST)
    public String contactUsers(@ModelAttribute("contactUserForm") @Valid NewsletterForm contactUserForm,
                               BindingResult result) {
        if(result.hasErrors()) {
            System.out.println("Invalid contactUserForm");
            return "admin";
        }
        List<String> recipients = Arrays.asList(contactUserForm.getRecipients().split(";"));
        System.out.println("Sending email with subject \"" + contactUserForm.getSubject() + "\" to " + recipients.size() + " users");
        newsletterservice.sendMassEmail("HelloSender", contactUserForm.getSubject(), contactUserForm.getMessage(), recipients);
        return "admin";
    }

    @RequestMapping(path="/sendNewsletter", method=POST)
    public String sendNewsletter(@ModelAttribute("newsletterForm") @Valid NewsletterForm newsletterForm,
                                 BindingResult result) {
        if(result.hasErrors()) {
            System.out.println("Invalid newsletterForm");
            return "admin";
        }
        List<String> recipients = Arrays.asList(newsletterForm.getRecipients().split(";"));
        System.out.println("Sending newsletter with subject \"" + newsletterForm.getSubject() + "\" to " + recipients.size() + " users");
        newsletterservice.sendMassEmail("NewsletterSender", newsletterForm.getSubject(), newsletterForm.getMessage(), recipients);
        return "admin";
    }

    @ModelAttribute
    public void prepare(Model model) {
        model.addAttribute("newsletterRecipients", newsletterservice.allRecipients());
        model.addAttribute("users", userService.allUsers());
        model.addAttribute("special", realWorld.isSpecialIsActive());
        model.addAttribute("nrvotes", realWorld.getNumberOfVotes());
        model.addAttribute("newsletterForm", new NewsletterForm());
        model.addAttribute("contactUserForm", new NewsletterForm());
    }

}
