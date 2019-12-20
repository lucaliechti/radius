package radius.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import radius.data.form.NewsletterForm;
import radius.web.components.RealWorldProperties;
import radius.web.service.NewsletterService;
import radius.web.service.SurveyService;
import radius.web.service.UserService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class AdminController {

    private NewsletterService newsletterservice;
    private UserService userService;
    private SurveyService surveyService;
    private RealWorldProperties realWorld;

    public AdminController(NewsletterService newsletterservice, UserService userService, SurveyService surveyService,
                           RealWorldProperties real) {
        this.newsletterservice = newsletterservice;
        this.userService = userService;
        this.surveyService = surveyService;
        this.realWorld = real;
    }

    @RequestMapping(path="/admin")
    public String admin() {
        return "admin";
    }

    @RequestMapping(path="/contactUsers", method=POST)
    public String contactUsers(@ModelAttribute("contactUserForm") @Valid NewsletterForm contactUserForm, Model model,
                               BindingResult result) {
        if(result.hasErrors()) {
            return "admin";
        }
        List<String> recipients = Arrays.asList(contactUserForm.getRecipients().split(";"));
        System.out.println("Sending email with subject \"" + contactUserForm.getSubject() + "\" to " + recipients.size() + " users");
        List<String> failed = newsletterservice.sendMassEmail(false, contactUserForm.getSubject(), contactUserForm.getMessage(), recipients, null);
        decorateModel(model, recipients, failed);
        return "admin";
    }
    @RequestMapping(path="/sendNewsletter", method=POST)
    public String sendNewsletter(@ModelAttribute("newsletterForm") @Valid NewsletterForm newsletterForm, Model model,
                                 BindingResult result) {
        if(result.hasErrors() || newsletterForm.getLanguage() == null) {
            return "admin";
        }
        List<String> recipients = Arrays.asList(newsletterForm.getRecipients().split(";"));
        System.out.println("Sending newsletter with subject \"" + newsletterForm.getSubject() + "\" to " + recipients.size() + " users, footer will be " + newsletterForm.getLanguage());
        List<String> failed = newsletterservice.sendMassEmail(true, newsletterForm.getSubject(), newsletterForm.getMessage(), recipients, new Locale(newsletterForm.getLanguage()));
        decorateModel(model, recipients, failed);
        return "admin";
    }

    private void decorateModel(Model model, List<String> recipients, List<String> failed) {
        int success = recipients.size() - failed.size();
        if(success > 0) {
            model.addAttribute("successfullySent", success);
        }
        if(failed.size() > 0) {
            model.addAttribute("failedRecipients", String.join("\n", failed));
        }
    }

    @ModelAttribute
    public void prepare(Model model) {
        model.addAttribute("surveyStats", surveyService.statistics());
        model.addAttribute("newsletterRecipients", newsletterservice.allRecipients());
        model.addAttribute("users", userService.allUsers());
        model.addAttribute("special", realWorld.isSpecialIsActive());
        model.addAttribute("nrvotes", realWorld.getNumberOfVotes());
        model.addAttribute("newsletterForm", new NewsletterForm());
        model.addAttribute("contactUserForm", new NewsletterForm());
    }

}
