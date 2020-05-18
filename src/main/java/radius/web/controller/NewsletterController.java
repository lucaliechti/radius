package radius.web.controller;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class NewsletterController {

    private final ModelDecorator modelDecorator;
    private final NewsletterService newsletterService;

    private static final String REGISTRATION_WEBSITE = "Website";

    @RequestMapping(path="/subscribe", method=GET)
    public String getSubscribe(Model model, Locale loc) {
        model.addAllAttributes(modelDecorator.homeAttributes(loc));
        return "home";
    }

    @RequestMapping(path="/subscribe", method=POST)
    public String subscribe(@ModelAttribute("subscriptionForm") @Valid EmailDto subscriptionForm,
                            BindingResult result, Model model, Locale loc) {
        if(result.hasErrors()) {
            model.addAttribute("generic_error", Boolean.TRUE);
            model.addAllAttributes(modelDecorator.homeAttributes(loc));
            return "home";
        }
        boolean success = newsletterService.subscribe(subscriptionForm.getEmail(), loc, REGISTRATION_WEBSITE);
        if(!success) {
            model.addAttribute("generic_error", Boolean.TRUE);
            model.addAllAttributes(modelDecorator.homeAttributes(loc));
            return "home";
        }
        model.addAttribute("newsletter_subscribe_success", Boolean.TRUE);
        model.addAllAttributes(modelDecorator.homeAttributes(loc));
        return "home";
    }

    @RequestMapping(path="/unsubscribe", method=GET)
    public String unsubscribe(@RequestParam(value = "uuid", required = true) String uuid, Model model, Locale loc) {
        boolean success = newsletterService.unsubscribe(uuid);
        if(!success) {
            model.addAttribute("generic_error", Boolean.TRUE);
            model.addAllAttributes(modelDecorator.homeAttributes(loc));
            return "home";
        }
        model.addAttribute("newsletter_unsubscribe_success", Boolean.TRUE);
        model.addAllAttributes(modelDecorator.homeAttributes(loc));
        return "home";
    }
}
