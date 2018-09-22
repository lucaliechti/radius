package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import radius.MeetingFeedbackForm;
import radius.User;
import radius.User.userModus;
import radius.User.userStatus;
import radius.UserPair;
//import radius.AnswerForm;
import radius.data.JDBCStaticResourceRepository;
import radius.data.JDBCUserRepository;
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
	
//	private JDBCStaticResourceRepository staticRepo;
	private AnswerController ac;
	
	//specify here which implementation of UserRepository will be used
	@Autowired
	public StatusController(JDBCStaticResourceRepository _staticRepo, AnswerController _ac) {
//		this.staticRepo = _staticRepo;
		this.ac = _ac;
	}
	
	@RequestMapping(method=GET)
	public String statusPage(@RequestParam(value = "login", required = false) String loggedin, Model model) {
		System.out.println("in the StatusController class");
		if(loggedin != null) {
			model.addAttribute("loggedin", "user has just logged in");
		}
		String email = SecurityContextHolder.getContext().getAuthentication().getName().toString();
		User user = userRepo.findUserByEmail(email);
		if(!user.getEnabled()) {
			model.addAttribute("not_enabled", true);
			return "login";
		}
		if(!user.getAnswered()) {
//			model.addAttribute("lang", staticRepo.languages());
//			model.addAttribute("modi", staticRepo.modi());
//			model.addAttribute("answerForm", new AnswerForm());
//			return "answers";
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
			model.addAttribute("feedbackForm", new MeetingFeedbackForm());
			model.addAttribute("userlocations", staticRepo.prettyLocations(user.getLocations()));
		}
		return "status";
	}
}