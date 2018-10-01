package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import radius.MeetingFeedbackForm;
import radius.data.MatchingRepository;
import radius.data.UserRepository;

@Controller
@RequestMapping(value="/toggleStatus")
public class ToggleStatusController {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private MatchingRepository matchRepo;
	
	@Autowired
	private StatusController sc;
	
	@RequestMapping(method=GET)
	public String toggle(Model model, Locale locale) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName().toString();
		if(userRepo.userIsActive(email)) {
			userRepo.deactivateUser(email);
		}
		else {
			userRepo.activateUser(email);
		}
		return sc.statusPage(model, locale);
	}
	
	@RequestMapping(method=POST)
	public String togglePost(@ModelAttribute("feedbackForm") @Valid MeetingFeedbackForm feedbackForm, BindingResult result, Model model, Locale locale) {
		if(result.hasErrors()) {
			System.out.println("ToggleStatusController: Error toggling");
			model.addAttribute("success", 0);
			return sc.statusPage(model, locale);
		}
		String email = SecurityContextHolder.getContext().getAuthentication().getName().toString();
		
		if(feedbackForm.getConfirmed()) {
			matchRepo.confirmHalfEdge(email);
		}
		else {
			matchRepo.unconfirmHalfEdge(email);
		}
		
		matchRepo.deactivateOldMatchesFor(email);
		
		switch(feedbackForm.getNextState()) {
		case "WAITING":
			model.addAttribute("success", 1);
			userRepo.activateUser(email);
			break;
		case "INACTIVE":
			model.addAttribute("success", 1);
			userRepo.deactivateUser(email);
			break;
		default:
			model.addAttribute("success", 0);
		}

		return sc.statusPage(model, locale);
	}
}
