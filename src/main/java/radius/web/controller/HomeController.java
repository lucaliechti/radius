package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import radius.User;
import radius.web.components.ModelDecorator;
import radius.web.service.*;

import java.util.Locale;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class HomeController {

	private final UserService userService;
	private final AnswerService answerService;
	private final ModelDecorator modelDecorator;

	@RequestMapping(value="/", method=GET)
	public String home(Model model, Locale locale) {
		if(userIsAuthenticated()) {
			if (userIsAdmin()) {
				model.addAllAttributes(modelDecorator.adminAttributes());
				return "admin";
			}
			return prepareModelAndRedirectLoggedInUser(model, locale);
		}
		if(model.containsAttribute("success")) {
			model.addAttribute("success", Boolean.TRUE);
		}
		model.addAllAttributes(modelDecorator.homeAttributes(locale));
		return "home";
	}

	@RequestMapping(value="/home", method=GET)
	public String frontPage(@RequestParam(value="logout", required=false) String loggedout,
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
		model.addAllAttributes(modelDecorator.homeAttributes(locale));
		return "home";
	}

	@RequestMapping(value={"/", "/home"}, method=POST)
	public String login(@RequestParam(value="error", required=false) String loginerror, Model model, Locale loc) {
		if(loginerror != null) {
			model.addAttribute("loginerror", Boolean.TRUE);
		}
		model.addAllAttributes(modelDecorator.homeAttributes(loc));
		return "home";
	}

	@RequestMapping(value="/status", method=GET)
	public String status(Model model, Locale locale) {
		return prepareModelAndRedirectLoggedInUser(model, locale);
	}

	private String prepareModelAndRedirectLoggedInUser(Model model, Locale locale) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<User> optionalUser = userService.findUserByEmail(email);
		if(optionalUser.isEmpty()) {
			model.addAttribute("generic_error", Boolean.TRUE);
			model.addAllAttributes(modelDecorator.homeAttributes(locale));
			return "home";
		}
		User user = optionalUser.get();
		if(!user.isEnabled()) {
			model.addAttribute("not_enabled", true);
			model.addAllAttributes(modelDecorator.homeAttributes(locale));
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
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth != null
			&& auth.isAuthenticated()
			&& !(auth instanceof AnonymousAuthenticationToken);
	}

	private boolean userIsAdmin() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return userIsAuthenticated() && auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}
}
