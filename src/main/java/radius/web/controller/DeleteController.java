package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import radius.web.components.ModelDecorator;
import radius.web.service.MatchingService;
import radius.web.service.UserService;

@Controller
@RequestMapping(value="/delete")
public class DeleteController {

	private ModelDecorator modelDecorator;
	private UserService userService;
	private MatchingService matchingService;

	public DeleteController(ModelDecorator modelDecorator, UserService userService, MatchingService matchingService) {
		this.modelDecorator = modelDecorator;
		this.userService = userService;
		this.matchingService = matchingService;
	}

	@RequestMapping(method=GET)
	public String reset(Model model) {
		model.addAllAttributes(modelDecorator.homeAttributes());
		return "home";
	}

	@RequestMapping(method=POST)
	public String contact(Model model) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		if(!matchingService.allMatchesForUser(username).isEmpty()) {
			model.addAttribute("delete_failed", true);
			return "profile";
		}
		userService.deleteUser(username);
		model.addAttribute("delete_success", username);
		SecurityContextHolder.clearContext();
		model.addAllAttributes(modelDecorator.homeAttributes());
		return "home";
	}
}
