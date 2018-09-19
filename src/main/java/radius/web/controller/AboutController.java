package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/about")
public class AboutController {

	@RequestMapping(method=GET)
	public String imprint(Model model) {
		System.out.println("in the AboutController class");
		return "about";
	}
}