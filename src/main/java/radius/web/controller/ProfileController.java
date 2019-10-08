package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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
import radius.data.StaticResourceRepository;
import radius.web.components.RealWorldConfiguration.RealWorldProperties;

@Controller
@RequestMapping(value="/profile")
public class ProfileController {
	
	private JDBCUserRepository userRepo;
	private StaticResourceRepository staticRepo;
	private HomeController hc;

	@Autowired
	private RealWorldProperties real;

	@Autowired
	public ProfileController(JDBCUserRepository _userRepo, JDBCStaticResourceRepository _staticRepo, HomeController _hc) {
		this.userRepo = _userRepo;
		this.staticRepo = _staticRepo;
		this.hc = _hc;
	}

	@RequestMapping(method=GET)
	public String profile(@RequestParam(value = "login", required = false) String loggedin, Model model, Locale locale) {
		if(loggedin != null) {
			model.addAttribute("loggedin", "user has just logged in");
		}
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		User u = userRepo.findUserByEmail(email);
		if(!u.getEnabled()) {
			model.addAttribute("not_enabled", true);
			return hc.home(null, null, model, null, null, locale);
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
			List<String> answers = u.getRegularAnswersAsListOfStrings().stream().map(s -> s.toLowerCase()).collect(Collectors.toList());
			model.addAttribute("answers", answers);
			model.addAttribute("modus", u.getModusAsString());
//			model.addAttribute("user", u); //this would be enough (except for questions probably). Make nicer.
			model.addAttribute("locations", staticRepo.prettyLocations(u.getLocations()));
			model.addAttribute("languages", u.getLanguages());
			model.addAttribute("history", usersMatches(email));
			model.addAttribute("nrQ", real.numberOfRegularQuestions());
		}
		return "profile";
	}
	
	//TODO: This is 1:1 the same as in StatusController. I can't inject a StatusController here because of circularity. Make nicer.
	public List<HalfEdge> usersMatches(String email) {
		return userRepo.allMatchesForUser(email);
	}

}

