package reach.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value="/profile")
public class ProfileController {

	@RequestMapping(method=GET)
	public String profile(@RequestParam(value = "login", required = false) String loggedin, Model model) {
		System.out.println("in the ProfileController class");
		if(loggedin != null) {
			model.addAttribute("loggedin", "user has just logged in");
		}
		return "profile";
	}
}

/*












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
			model.addAttribute("loginerror", "loginerror");
		}
		return "login";
	}
}















*/