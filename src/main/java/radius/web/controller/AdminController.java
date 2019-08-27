package radius.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import radius.NewsletterMessage;
import radius.data.NewsletterRepository;
import radius.data.UserRepository;
import radius.web.components.RealWorldProperties;

import java.util.Locale;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class AdminController {

    @Autowired
    private NewsletterRepository newsletterRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RealWorldProperties realWorld;

    @RequestMapping(path="/admin", method=GET)
    public String admin(Model model, Locale loc) {
        model.addAttribute("newsletterForm", new NewsletterMessage());
        model.addAttribute("numberRecipients", newsletterRepo.numberOfRecipients());
        model.addAttribute("users", userRepo.allUsers());
        model.addAttribute("special", realWorld.specialIsActive());
        model.addAttribute("nrvotes", realWorld.numberOfVotes());
        return "admin";
    }

}
