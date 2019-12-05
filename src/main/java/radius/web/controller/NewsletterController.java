package radius.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import radius.data.dto.EmailDto;
import radius.web.components.ModelDecorator;
import radius.web.service.NewsletterService;

import javax.validation.Valid;
import java.util.Locale;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class NewsletterController {

    private ModelDecorator modelDecorator;
    private NewsletterService newsletterService;

    private static final String REGISTRATION_WEBSITE = "Website";

    public NewsletterController(ModelDecorator modelDecorator, NewsletterService newsletterService) {
         this.modelDecorator = modelDecorator;
         this.newsletterService = newsletterService;
    }

    @RequestMapping(path="/subscribe", method=GET)
    public String getSubscribe(Model model) {
        model.addAllAttributes(modelDecorator.homeAttributes());
        return "home";
    }

    @RequestMapping(path="/subscribe", method=POST)
    public String subscribe(@ModelAttribute("subscriptionForm") @Valid EmailDto subscriptionForm,
                            BindingResult result, Model model, Locale loc) {
        if(result.hasErrors()) {
            model.addAttribute("generic_error", Boolean.TRUE);
            model.addAllAttributes(modelDecorator.homeAttributes());
            return "home";
        }
        boolean success = newsletterService.subscribe(subscriptionForm.getEmail(), loc, REGISTRATION_WEBSITE);
        if(!success) {
            model.addAttribute("generic_error", Boolean.TRUE);
            model.addAllAttributes(modelDecorator.homeAttributes());
            return "home";
        }
        model.addAttribute("newsletter_subscribe_success", Boolean.TRUE);
        model.addAllAttributes(modelDecorator.homeAttributes());
        return "home";
    }

    @RequestMapping(path="/unsubscribe", method=GET)
    public String unsubscribe(@RequestParam(value = "uuid", required = true) String uuid, Model model) {
        boolean success = newsletterService.unsubscribe(uuid);
        if(!success) {
            model.addAttribute("generic_error", Boolean.TRUE);
            model.addAllAttributes(modelDecorator.homeAttributes());
            return "home";
        }
        model.addAttribute("newsletter_unsubscribe_success", Boolean.TRUE);
        model.addAllAttributes(modelDecorator.homeAttributes());
        return "home";
    }
}
