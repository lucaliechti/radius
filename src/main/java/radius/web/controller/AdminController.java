package radius.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import radius.web.components.RealWorldProperties;
import radius.web.service.NewsletterService;
import radius.web.service.UserService;

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

    @ModelAttribute
    public void prepare(Model model) {
        model.addAttribute("numberRecipients", newsletterservice.numberOfRecipients());
        model.addAttribute("users", userService.allUsers());
        model.addAttribute("special", realWorld.isSpecialIsActive());
        model.addAttribute("nrvotes", realWorld.getNumberOfVotes());
    }

}
