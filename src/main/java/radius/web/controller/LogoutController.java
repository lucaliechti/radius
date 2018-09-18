package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/logout")
public class LogoutController {
	
	@Autowired
	private HomeController hc;
	
	/*
	@RequestMapping(method=GET)
	public String logout() {
		System.out.println("in the LogoutController class");
		return "/home";
	}*/
	
	@RequestMapping(method=GET)
	public String logoutPage (Model model, HttpServletRequest request, HttpServletResponse response) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null){    
	        new SecurityContextLogoutHandler().logout(request, response, auth);
	    }
	    return hc.home("loggedout", null, model, request, response);
//	    return "home?logout";
	}
}