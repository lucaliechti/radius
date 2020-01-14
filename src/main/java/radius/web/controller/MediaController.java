package radius.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import radius.data.repository.MentionRepository;

@Controller
public class MediaController {

    private MentionRepository mentionRepo;

    public MediaController(MentionRepository mentionRepo) {
        this.mentionRepo = mentionRepo;
    }

    @RequestMapping(value="/media")
    public String media() {
        return "media";
    }

    @ModelAttribute
    public void addMedia(Model model) {
        model.addAttribute("mentions", mentionRepo.allMentions());
    }

}
