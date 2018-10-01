package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	public String logoutPage (Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) {
		System.out.println("in the LogoutController class");
	    return hc.home("loggedout", null, model, request, response, locale);
	}
}