package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/myexperience")
public class MyExperienceController {

	@RequestMapping(method=GET)
	public String contact(Model model) {
		return "myexperience";
	}
}
