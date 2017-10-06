package reach.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import java.util.Locale;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value={"/", "/home"})
public class HomeController {

	@RequestMapping(method=GET)
	public String home(@RequestParam(value = "logout", required = false) String loggedout, Locale loc, Model model) {
//		System.out.println("in the HomeController class");
//		System.out.println("Locale = " + loc);

		if(loggedout != null) {
			model.addAttribute("loggedout", "user sees this page right after logging out");
		}
		
		return "home";
	}
}
