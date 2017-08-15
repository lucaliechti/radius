package reach.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import java.util.Locale;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value={"/", "/home"})
public class HomeController {

	@RequestMapping(method=GET)
	public String home(Locale loc, Model model) {
		System.out.println("in the HomeController class");
		System.out.println("Locale = " + loc);
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("username", username);
		return "home";
	}
}
