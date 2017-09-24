package reach.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/privacy")
public class PrivacyController {

	@RequestMapping(method=GET)
	public String privacy(Model model) {
		System.out.println("in the PrivacyController class");
//		String username = SecurityContextHolder.getContext().getAuthentication().getName();
//		model.addAttribute("username", username);
		return "privacy";
	}
}