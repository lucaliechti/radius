package radius.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


@Controller
@RequestMapping(value="/login")
public class LoginController {

	@RequestMapping(method=GET)
	public String login(@RequestParam(value = "error", required = false) String loginerror, Model model) {
		System.out.println("in the LoginController class");
		if(loginerror != null) {
			model.addAttribute("loginerror", new Boolean(true));
		}
		return "login";
	}
}