package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import radius.User;
import radius.web.components.ModelRepository;
import radius.web.service.UserService;

import java.util.Optional;

@Controller
@ComponentScan("radius.config")
@Slf4j
public class HomeController {

	private UserService userService;
	private ModelRepository modelRepository;

	public HomeController(UserService userService, ModelRepository modelRepository) {
		this.userService = userService;
		this.modelRepository = modelRepository;
	}
	
	@RequestMapping(value={"/", "/home"}, method=GET)
	public String home(@RequestParam(value="logout", required=false) String loggedout,
					   @RequestParam(value="error", required=false) String error, Model model) {
		log.info("In the HomeController class");
		if(userIsAuthenticated()) {
			return prepareModelAndRedirectLoggedInUser(model);
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
		model.addAllAttributes(modelRepository.homeAttributes());
		return "home";
	}
	
	@RequestMapping(value={"/", "/home"}, method=POST)
	public String login(@RequestParam(value="error", required=false) String loginerror, Model model) {
		if(loginerror != null) {
			model.addAttribute("loginerror", Boolean.TRUE);
		}
		model.addAllAttributes(modelRepository.homeAttributes());
		return "home";
	}

	@RequestMapping(value="/status", method=GET)
	public String status(Model model) {
		return prepareModelAndRedirectLoggedInUser(model);
	}

	private String prepareModelAndRedirectLoggedInUser(Model model) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<User> optionalUser = userService.findUserByEmail(email);
		if(optionalUser.isEmpty()) {
			model.addAttribute("generic_error", Boolean.TRUE);
			model.addAllAttributes(modelRepository.homeAttributes());
			return "home";
		}
		User user = optionalUser.get();
		if(!user.isEnabled()) {
			model.addAttribute("not_enabled", true);
			model.addAllAttributes(modelRepository.homeAttributes());
			return "home";
		}
		else if(!user.isAnsweredRegular()) {
			model.addAllAttributes(modelRepository.answerAttributes());
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
