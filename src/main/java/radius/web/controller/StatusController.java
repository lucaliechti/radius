package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import radius.HalfEdge;
import radius.data.form.MeetingFeedbackForm;
import radius.User;
import radius.User.userStatus;
import radius.UserPair;
import radius.data.*;

@Controller
@RequestMapping(value="/status")
public class StatusController {

	private UserRepository userRepo;
	private MatchingRepository matchRepo;
	private StaticResourceRepository staticRepo;
	private AnswerController ac;
	private HomeController hc;

	@Autowired
	public StatusController(JDBCUserRepository userRepo, MatchingRepository matchingRepo,
							JDBCStaticResourceRepository staticRepo, AnswerController ac) {
		this.userRepo = userRepo;
		this.matchRepo = matchingRepo;
		this.staticRepo = staticRepo;
		this.ac = ac;
	}

	@Autowired
	public void setHomeController(HomeController hc) {
		this.hc = hc;
	}
	
	@RequestMapping(method=GET)
	public String statusPage(Model model, Locale locale) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepo.findUserByEmail(email);
		if(!user.getEnabled()) {
			model.addAttribute("not_enabled", true);
			return hc.cleanlyHome(model);
		}
		if(!user.getAnswered()) {
			return ac.answer(model);
		}
		else {
			model.addAttribute("user", user);
			model.addAttribute("history", usersMatches(email));
			model.addAttribute("feedbackForm", new MeetingFeedbackForm());
			model.addAttribute("userlocations", staticRepo.prettyLocations(user.getLocations()));
			if(user.getStatus() == userStatus.MATCHED){
				User match = matchRepo.getCurrentMatchFor(email);
				model.addAttribute("match", match);
				UserPair up = UserPair.of(user, match);
				model.addAttribute("compatibleModus",
						User.convertModusToString(UserPair.commonModus(user.getModus(), match.getModus()).get()));
				model.addAttribute("commonlocations",
						String.join(", ", staticRepo.prettyLocations(new ArrayList<>(up.commonLocations()))));
				model.addAttribute("commonlanguages", up.commonLanguages());
			}
		}
		return "status";
	}

	public List<HalfEdge> usersMatches(String email) {
		return userRepo.allMatchesForUser(email);
	}
}
