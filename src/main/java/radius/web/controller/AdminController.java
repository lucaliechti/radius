package radius.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import radius.data.form.ConfigurationForm;
import radius.data.form.NewsletterForm;
import radius.web.service.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class AdminController {

    private NewsletterService newsletterservice;
    private UserService userService;
    private SurveyService surveyService;
    private MatchingService matchingService;
    private ConfigService configService;

    public AdminController(NewsletterService newsletterservice, UserService userService, SurveyService surveyService,
                           MatchingService matchingService, ConfigService configService) {
        this.newsletterservice = newsletterservice;
        this.userService = userService;
        this.surveyService = surveyService;
        this.matchingService = matchingService;
        this.configService = configService;
    }

    @RequestMapping(path="/admin")
    public String admin() {
        return "admin";
    }

    @RequestMapping(path="/banUser", method=GET)
    public String banUser(@RequestParam(value = "uuid") String uuid, Model model) {
        Optional<String> optionalUser = userService.findEmailByUuid(uuid);
        if(optionalUser.isPresent()) {
            if(userService.banUser(optionalUser.get())) {
                model.addAttribute("success", Boolean.TRUE);
            } else {
                model.addAttribute("failure", Boolean.TRUE);
            }
        }
        model.addAttribute("users", userService.allUsers());
        return "admin";
    }

    @RequestMapping(path="/deleteUser", method=GET)
    public String deleteUser(@RequestParam(value = "uuid") String uuid, Model model) {
        Optional<String> optionalUser = userService.findEmailByUuid(uuid);
        if(optionalUser.isPresent()) {
            if(userService.deleteUser(optionalUser.get())) {
                model.addAttribute("success", Boolean.TRUE);
            } else {
                model.addAttribute("failure", Boolean.TRUE);
            }
        }
        model.addAttribute("users", userService.allUsers());
        return "admin";
    }

    @RequestMapping(path="/setPrivate", method=GET)
    public String setPrivate(@RequestParam(value = "uuid") String uuid, Model model) {
        Optional<String> optionalUser = userService.findEmailByUuid(uuid);
        if(optionalUser.isPresent()) {
            if(userService.setUserPrivate(optionalUser.get())) {
                model.addAttribute("success", Boolean.TRUE);
            } else {
                model.addAttribute("failure", Boolean.TRUE);
            }
        }
        model.addAttribute("users", userService.allUsers());
        return "admin";
    }

    @RequestMapping(path="/unsubscribeNewsletter", method=GET)
    public String unsubscribeNewsletter(@RequestParam(value = "uuid") String uuid, Model model) {
        if(newsletterservice.unsubscribe(uuid)){
            model.addAttribute("success", Boolean.TRUE);
        } else {
            model.addAttribute("failure", Boolean.TRUE);
        }
        model.addAttribute("newsletterRecipients", newsletterservice.allRecipients());
        return "admin";
    }

    @RequestMapping(path="/updateConfiguration", method=POST)
    public String updateConfiguration(@ModelAttribute("configurationForm") @Valid ConfigurationForm form, Model model,
                                      BindingResult result) {
        if(result.hasErrors()) {
            return "admin";
        }
        configService.updateConfig(form);
        model.addAttribute("configupdate_success", Boolean.TRUE);
        model.addAttribute("configurationForm", configService.getForm());
        return "admin";
    }

    @RequestMapping(path="/contactUsers", method=POST)
    public String contactUsers(@ModelAttribute("contactUserForm") @Valid NewsletterForm contactUserForm, Model model,
                               BindingResult result) {
        if(result.hasErrors()) {
            return "admin";
        }
        List<String> recipients = Arrays.asList(contactUserForm.getRecipients().split(";"));
        List<String> failed = newsletterservice.sendMassEmail(false,
                contactUserForm.getSubject(),
                contactUserForm.getMessage(),
                recipients,
                new Locale(contactUserForm.getLanguage()));
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
        List<String> failed = newsletterservice.sendMassEmail(true,
                newsletterForm.getSubject(),
                newsletterForm.getMessage(),
                recipients,
                new Locale(newsletterForm.getLanguage()));
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

    @RequestMapping(path="/updateConfiguration", method=GET)
    public String getUpdateConfiguration() {
        return "admin";
    }

    @RequestMapping(path="/contactUsers", method=GET)
    public String getContactUsers() {
        return "admin";
    }

    @RequestMapping(path="/sendNewsletter", method=GET)
    public String getSendNewsletter() {
        return "admin";
    }

    @ModelAttribute
    public void prepare(Model model) {
        model.addAttribute("configurationForm", configService.getForm());
        model.addAttribute("matches", matchingService.allMatches().stream().
                filter(m -> m.email1().compareToIgnoreCase(m.email2()) < 0).collect(Collectors.toList()));
        model.addAttribute("regionDensity", userService.regionDensity());
        model.addAttribute("surveyStats", surveyService.statistics());
        model.addAttribute("newsletterRecipients", newsletterservice.allRecipients());
        model.addAttribute("users", userService.allUsers());
        model.addAttribute("special", configService.specialActive());
        model.addAttribute("nrvotes", configService.numberOfVotes());
        model.addAttribute("newsletterForm", new NewsletterForm());
        model.addAttribute("contactUserForm", new NewsletterForm());
    }

}
