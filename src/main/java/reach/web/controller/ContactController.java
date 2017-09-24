package reach.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/contact")
public class ContactController {

	@RequestMapping(method=GET)
	public String contact(Model model) {
		System.out.println("in the ContactController class");
//		String username = SecurityContextHolder.getContext().getAuthentication().getName();
//		model.addAttribute("username", username);
		return "contact";
	}
}
