package radius.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StaticPagesController {

    @RequestMapping(value="/about")
    public String about() {
        return "about";
    }

    @RequestMapping(value="/imprint")
    public String imprint() {
        return "imprint";
    }

    @RequestMapping(value="/privacy")
    public String privacy() {
        return "privacy";
    }

    @RequestMapping(value="/support")
    public String support() {
        return "support";
    }
}
