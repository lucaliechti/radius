package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import radius.UserForm;
import radius.data.JDBCStaticResourceRepository;

@Controller
@RequestMapping(value={"/", "/home"})
@ComponentScan("radius.config") 
public class HomeController {

	private JDBCStaticResourceRepository staticRepo;
	
	@Autowired
	private StatusController sc;
	
	@Autowired
	public HomeController(JDBCStaticResourceRepository _staticRepo) {
		this.staticRepo = _staticRepo;
	}
	
	@RequestMapping(method=GET)
	public String home(@RequestParam(value = "logout", required = false) String loggedout, @RequestParam(value = "error", required = false) String error, Model model, HttpServletRequest req, HttpServletResponse res, Locale locale) {

		System.out.println("In the HomeController class");
		
		if(SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated() && !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) ) {
			return sc.statusPage(model, locale);
		}
		
		model.addAttribute("registrationForm", new UserForm());
		model.addAttribute("cantons", staticRepo.cantons());
		
		if(loggedout != null) {
			model.addAttribute("loggedout", "user sees this page right after logging out");
		}
		
		if(error != null) {
			model.addAttribute("loginerror", "user sees this page right after logging out");
		}

		if(model.containsAttribute("success")) {
			model.addAttribute("success", -1);
		}
		
		return "home";
	}
}
