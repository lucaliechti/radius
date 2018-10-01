package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import radius.HalfEdge;
import radius.MeetingFeedbackForm;
import radius.User;
import radius.User.userStatus;
import radius.UserPair;
import radius.data.MatchingRepository;
import radius.data.StaticResourceRepository;
import radius.data.UserRepository;

@Controller
@RequestMapping(value="/status")
public class StatusController {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private MatchingRepository matchRepo;
	
	@Autowired
	private StaticResourceRepository staticRepo;
	
    @Autowired
	private AnswerController ac;
	
	@RequestMapping(method=GET)
	public String statusPage(Model model, Locale locale) {
		System.out.println("in the StatusController class");
		String email = SecurityContextHolder.getContext().getAuthentication().getName().toString();
		User user = userRepo.findUserByEmail(email);
		if(!user.getEnabled()) {
			model.addAttribute("not_enabled", true);
			return "login";
		}
		if(!user.getAnswered()) {
			return ac.answer(model);
		}
		else {
			model.addAttribute("user", user);
			if(user.getStatus() == userStatus.MATCHED){
				User match = matchRepo.getCurrentMatchFor(email);
				model.addAttribute("match", match);
				UserPair up = UserPair.of(user, match);
				model.addAttribute("modi", User.convertModusToString(UserPair.commonModus(user.getModus(), match.getModus()).get()));
				model.addAttribute("commonlocations", String.join(", ", staticRepo.prettyLocations(new ArrayList<Integer>(up.commonLocations()))));
				model.addAttribute("commonlanguages", up.commonLanguages());		
			}
			model.addAttribute("history", usersMatches(email));
			model.addAttribute("feedbackForm", new MeetingFeedbackForm());
			model.addAttribute("userlocations", staticRepo.prettyLocations(user.getLocations()));
		}
		return "status";
	}
	
	public List<HalfEdge> usersMatches(String email) {
		return userRepo.allMatchesForUser(email);
	}
	

}