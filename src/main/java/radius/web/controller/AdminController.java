package radius.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import radius.data.repository.JDBCNewsletterRepository;
import radius.data.repository.JDBCUserRepository;
import radius.data.repository.NewsletterRepository;
import radius.data.repository.UserRepository;
import radius.web.components.RealWorldProperties;

@Controller
public class AdminController {

    private NewsletterRepository newsletterRepo;
    private UserRepository userRepo;
    private RealWorldProperties realWorld;

    public AdminController(JDBCNewsletterRepository newsRepo, JDBCUserRepository userRepo, RealWorldProperties real) {
        this.newsletterRepo = newsRepo;
        this.userRepo = userRepo;
        this.realWorld = real;
    }

    @RequestMapping(path="/admin")
    public String admin() {
        return "admin";
    }

    @ModelAttribute
    public void prepare(Model model) {
        model.addAttribute("numberRecipients", newsletterRepo.numberOfRecipients());
        model.addAttribute("users", userRepo.allUsers());
        model.addAttribute("special", realWorld.isSpecialIsActive());
        model.addAttribute("nrvotes", realWorld.getNumberOfVotes());
    }

}
