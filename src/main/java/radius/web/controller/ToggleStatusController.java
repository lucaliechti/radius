package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.validation.Valid;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import radius.User;
import radius.data.form.MeetingFeedbackForm;
import radius.web.components.ModelDecorator;
import radius.web.service.MatchingService;
import radius.web.service.UserService;

import java.util.Optional;

@Controller
@RequestMapping(value="/toggleStatus")
public class ToggleStatusController {

	private UserService userService;
	private ModelDecorator modelDecorator;
	private MatchingService matchingService;

	public ToggleStatusController(UserService userService, ModelDecorator modelDecorator,
								  MatchingService matchingService) {
		this.userService = userService;
		this.modelDecorator = modelDecorator;
		this.matchingService = matchingService;
	}
	
	@RequestMapping(method=GET)
	public String toggle(Model model) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<User> optionalUser = userService.findUserByEmail(email);
		if(!optionalUser.isPresent()) {
			model.addAttribute("generic_error", Boolean.TRUE);
			model.addAllAttributes(modelDecorator.homeAttributes());
			return "home";
		}
		User user = optionalUser.get();
		if(user.getStatus() != User.UserStatus.INACTIVE) {
			userService.deactivateUser(user);
		} else {
			userService.activateUser(user);
		}
		model.addAllAttributes(userService.userSpecificAttributes(user));
		return "status";
	}
	
	@RequestMapping(method=POST)
	public String togglePost(@ModelAttribute("feedbackForm") @Valid MeetingFeedbackForm feedbackForm,
							 BindingResult result, Model model) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<User> optionalUser = userService.findUserByEmail(email);
		if(!optionalUser.isPresent()) {
			model.addAttribute("generic_error", Boolean.TRUE);
			model.addAllAttributes(modelDecorator.homeAttributes());
			return "home";
		}
		User user = optionalUser.get();
		if(result.hasErrors()) {
			model.addAttribute("success", 0);
			model.addAllAttributes(userService.userSpecificAttributes(user));
			return "status";
		}
		matchingService.updateMatchesAfterMeeting(user, feedbackForm);
		boolean success = userService.updateUserAfterMeeting(user, feedbackForm);
		model.addAllAttributes(userService.userSpecificAttributes(user));
		model.addAttribute("success", success ? 1 : 0);
		return "status";
	}
}
