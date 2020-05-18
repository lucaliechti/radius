package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
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

import java.util.Locale;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping(value="/toggleStatus")
public class ToggleStatusController {

	private final UserService userService;
	private final ModelDecorator modelDecorator;
	private final MatchingService matchingService;
	
	@RequestMapping(method=GET)
	public String toggle(Model model, Locale loc) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<User> optionalUser = userService.findUserByEmail(email);
		if(optionalUser.isEmpty()) {
			model.addAttribute("generic_error", Boolean.TRUE);
			model.addAllAttributes(modelDecorator.homeAttributes(loc));
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
							 BindingResult result, Model model, Locale loc) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<User> optionalUser = userService.findUserByEmail(email);
		if(optionalUser.isEmpty()) {
			model.addAttribute("generic_error", Boolean.TRUE);
			model.addAllAttributes(modelDecorator.homeAttributes(loc));
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
