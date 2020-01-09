package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.LocaleResolver;
import radius.User;
import radius.web.components.ModelDecorator;
import radius.web.service.AnswerService;
import radius.web.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Optional;

@Controller
@ComponentScan("radius.config")
public class HomeController {

	private UserService userService;
	private AnswerService answerService;
	private ModelDecorator modelDecorator;

	public HomeController(UserService userService, AnswerService answerService, ModelDecorator modelDecorator,
						  LocaleResolver resolver) {
		this.userService = userService;
		this.answerService = answerService;
		this.modelDecorator = modelDecorator;
	}
	
	@RequestMapping(value={"/", "/home"}, method=GET)
	public String home(@RequestParam(value="logout", required=false) String loggedout,
					   @RequestParam(value="error", required=false) String error, Model model, Locale locale) {
		if(userIsAuthenticated()) {
			return prepareModelAndRedirectLoggedInUser(model, locale);
		}
		if(loggedout != null) {
			model.addAttribute("loggedout", Boolean.TRUE);
		}
		if(error != null) {
			model.addAttribute("loginerror", Boolean.TRUE);
		}
		if(model.containsAttribute("success")) {
			model.addAttribute("success", Boolean.TRUE);
		}
		model.addAllAttributes(modelDecorator.homeAttributes());
		return "home";
	}

	@RequestMapping(value={"/", "/home"}, method=POST)
	public String login(@RequestParam(value="error", required=false) String loginerror, Model model) {
		if(loginerror != null) {
			model.addAttribute("loginerror", Boolean.TRUE);
		}
		model.addAllAttributes(modelDecorator.homeAttributes());
		return "home";
	}

	@RequestMapping(value="/status", method=GET)
	public String status(Model model, Locale locale) {
		return prepareModelAndRedirectLoggedInUser(model, locale);
	}

	private String prepareModelAndRedirectLoggedInUser(Model model, Locale locale) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<User> optionalUser = userService.findUserByEmail(email);
		if(!optionalUser.isPresent()) {
			model.addAttribute("generic_error", Boolean.TRUE);
			model.addAllAttributes(modelDecorator.homeAttributes());
			return "home";
		}
		User user = optionalUser.get();
		if(!user.isEnabled()) {
			model.addAttribute("not_enabled", true);
			model.addAllAttributes(modelDecorator.homeAttributes());
			return "home";
		}
		else if(!answerService.userHasValidlyAnswered(user)) {
			model.addAllAttributes(modelDecorator.answerAttributes(locale));
			model.addAttribute("answerForm", answerService.newFormFromUser(user));
			return "answers";
		} else {
			model.addAllAttributes(userService.userSpecificAttributes(user));
			return "status";
		}
	}

	private boolean userIsAuthenticated() {
		return SecurityContextHolder.getContext().getAuthentication() != null &&
			SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
			!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken);
	}
}
