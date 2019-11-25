package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/logout")
public class LogoutController {
	
	@Autowired
	private HomeController hc;
	
	@RequestMapping(method=GET)
	public String logoutPage (Model model) {
	    return hc.home("loggedout", null, model);
	}
}
