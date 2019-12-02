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

import radius.data.repository.JDBCMatchingRepository;
import radius.data.repository.JDBCUserRepository;
import radius.data.form.MeetingFeedbackForm;
import radius.data.repository.MatchingRepository;
import radius.data.repository.UserRepository;

@Controller
@RequestMapping(value="/toggleStatus")
public class ToggleStatusController {

	private UserRepository userRepo;
	private MatchingRepository matchRepo;
	private StatusController sc;

	public ToggleStatusController(JDBCUserRepository userRepo, JDBCMatchingRepository matchRepo, StatusController sc) {
		this.userRepo = userRepo;
		this.matchRepo = matchRepo;
		this.sc = sc;
	}
	
	@RequestMapping(method=GET)
	public String toggle(Model model) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		if(userRepo.userIsActive(email)) {
			userRepo.deactivateUser(email);
		} else {
			userRepo.activateUser(email);
		}
		return sc.statusPage(model);
	}
	
	@RequestMapping(method=POST)
	public String togglePost(@ModelAttribute("feedbackForm") @Valid MeetingFeedbackForm feedbackForm,
							 BindingResult result, Model model) {
		if(result.hasErrors()) {
			model.addAttribute("success", 0);
			return sc.statusPage(model);
		}
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		
		if(feedbackForm.isConfirmed()) {
			matchRepo.confirmHalfEdge(email);
		} else {
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
		return sc.statusPage(model);
	}
}
