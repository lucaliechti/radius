package radius.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import radius.web.service.PressService;

@Controller
@RequiredArgsConstructor
public class MediaController {

    private final PressService pressService;

    @RequestMapping(value="/media")
    public String media() {
        return "media";
    }

    @ModelAttribute
    public void addMedia(Model model) {
        model.addAttribute("mentions", pressService.allMentions());
        model.addAttribute("pressreleases", pressService.allPressreleases());
    }

}
