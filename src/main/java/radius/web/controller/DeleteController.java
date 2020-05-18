package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import radius.web.components.ModelDecorator;
import radius.web.service.MatchingService;
import radius.web.service.UserService;

import java.util.Locale;

@Controller
@RequiredArgsConstructor
@RequestMapping(value="/delete")
public class DeleteController {

	private final ModelDecorator modelDecorator;
	private final UserService userService;
	private final MatchingService matchingService;

	@RequestMapping(method=GET)
	public String reset(Model model, Locale loc) {
		model.addAllAttributes(modelDecorator.homeAttributes(loc));
		return "home";
	}

	@RequestMapping(method=POST)
	public String contact(Model model, Locale loc) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		if(!matchingService.allMatchesForUser(username).isEmpty()) {
			model.addAttribute("delete_failed", true);
			return "profile";
		}
		userService.deleteUser(username);
		model.addAttribute("delete_success", username);
		SecurityContextHolder.clearContext();
		model.addAllAttributes(modelDecorator.homeAttributes(loc));
		return "home";
	}
}
