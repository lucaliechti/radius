package reach.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value="/login")
public class LoginController {

	@RequestMapping(method=GET)
	public String login(@RequestParam(value = "error", required = false) Boolean loginerror, Model model) {
		System.out.println("in the LoginController class");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("username", username);
		model.addAttribute("loginerror", loginerror);
		return "login";
	}
}