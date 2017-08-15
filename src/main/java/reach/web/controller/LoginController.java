package reach.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/login")
public class LoginController {

	@RequestMapping(method=GET)
	public String login(Model model) {
		System.out.println("in the LoginController class");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("username", username);
		return "login";
	}
}