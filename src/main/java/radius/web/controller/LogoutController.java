package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/logout")
public class LogoutController {

	@RequestMapping(method=GET)
	public String logout() {
		System.out.println("in the LogoutController class");
		return "/home";
	}
}