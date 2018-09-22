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

import radius.AnswerForm;
import radius.HalfEdge;
import radius.User;
import radius.data.JDBCStaticResourceRepository;
import radius.data.JDBCUserRepository;
import radius.data.MatchingRepository;
import radius.data.StaticResourceRepository;

@Controller
@RequestMapping(value="/profile")
public class ProfileController {
	
	@Autowired
	private MatchingRepository matchRepo;
	
	private JDBCUserRepository userRepo;
	private static StaticResourceRepository staticRepo;
	private HomeController hc;

	@Autowired
	public ProfileController(JDBCUserRepository _userRepo, JDBCStaticResourceRepository _staticRepo, HomeController _hc) {
		this.userRepo = _userRepo;
		this.staticRepo = _staticRepo;
		this.hc = _hc;
	}

	@RequestMapping(method=GET)
	public String profile(@RequestParam(value = "login", required = false) String loggedin, Model model) {
		if(loggedin != null) {
			model.addAttribute("loggedin", "user has just logged in");
		}
		String email = SecurityContextHolder.getContext().getAuthentication().getName().toString();
		User u = userRepo.findUserByEmail(email);
		if(!u.getEnabled()) {
			model.addAttribute("not_enabled", true);
			return hc.home(null, null, model, null, null);
		}
		else if(!u.getAnswered()) {
			model.addAttribute("lang", staticRepo.languages());
			model.addAttribute("modi", staticRepo.modi());
			model.addAttribute("answerForm", new AnswerForm());
			return "answers";
		}
		else {
			model.addAttribute("firstName", u.getFirstname());
			model.addAttribute("lastName", u.getLastname());
			model.addAttribute("email", u.getEmail());
			model.addAttribute("canton", u.getCanton());
			model.addAttribute("motivation", u.getMotivation());
			List<Boolean> q = u.getQuestions();
			model.addAttribute("q1", q.get(0));
			model.addAttribute("q2", q.get(1));
			model.addAttribute("q3", q.get(2));
			model.addAttribute("q4", q.get(3));
			model.addAttribute("q5", q.get(4));
			model.addAttribute("modus", u.getModusAsString());
			model.addAttribute("user", u); //this would be enough (except for questions probably). Make nicer.
//			List<Integer> loc = u.getLocations();
//			ArrayList<String> locations = new ArrayList<String>();
//			for (int l : loc) {
//				locations.add(staticRepo.regions().get(l));
//			}

//			model.addAttribute("locations", locations);
			model.addAttribute("locations", staticRepo.prettyLocations(u.getLocations()));
			model.addAttribute("languages", u.getLanguages());
//			model.addAttribute("status", u.getStatus().toString());
			model.addAttribute("history", usersMatches(email));
		}
		return "profile";
	}
	
	//TODO: This is 1:1 the same as in StatusController. I can't inject a StatusController here because of circularity. Make nicer.
	public List<HalfEdge> usersMatches(String email) {
		return userRepo.allMatchesForUser(email);
	}

}

