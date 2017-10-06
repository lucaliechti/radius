package reach.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value="/login")
public class LoginController {

	@RequestMapping(method=GET)
	public String login(@RequestParam(value = "error", required = false) String loginerror, Model model) {
		System.out.println("in the LoginController class");
		if(loginerror != null) {
			model.addAttribute("loginerror", "login failed");
		}
		return "login";
	}
}