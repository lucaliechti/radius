package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import radius.data.repository.UserRepository;
import radius.data.form.AnswerForm;
import radius.HalfEdge;
import radius.User;
import radius.data.repository.JDBCStaticResourceRepository;
import radius.data.repository.JDBCUserRepository;
import radius.data.repository.StaticResourceRepository;
import radius.web.components.RealWorldConfiguration.RealWorldProperties;

@Controller
@RequestMapping(value="/profile")
public class ProfileController {
	
	private UserRepository userRepo;
	private StaticResourceRepository staticRepo;
	private HomeController hc;
	private RealWorldProperties real;

	@Autowired
	public ProfileController(JDBCUserRepository userRepo, JDBCStaticResourceRepository staticRepo, HomeController hc,
							 RealWorldProperties real) {
		this.userRepo = userRepo;
		this.staticRepo = staticRepo;
		this.hc = hc;
		this.real = real;
	}

	@RequestMapping(method=GET)
	public String profile(@RequestParam(value = "login", required = false) String loggedin, Model model) {
		if(loggedin != null) {
			model.addAttribute("loggedin", "user has just logged in");
		}
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		User u = userRepo.findUserByEmail(email);
		if(!u.isEnabled()) {
			model.addAttribute("not_enabled", true);
			return hc.home(null, null, model);
		}
		else if(!u.isAnsweredRegular()) {
			model.addAttribute("lang", staticRepo.languages());
			model.addAttribute("answerForm", new AnswerForm());
			return "answers";
		}
		else {
			model.addAttribute("firstName", u.getFirstname());
			model.addAttribute("lastName", u.getLastname());
			model.addAttribute("email", u.getEmail());
			model.addAttribute("canton", u.getCanton());
			model.addAttribute("motivation", u.getMotivation());
			List<String> answers = u.getRegularAnswersAsListOfStrings().stream().map(String::toLowerCase)
					.collect(Collectors.toList());
			model.addAttribute("answers", answers);
//			model.addAttribute("user", u); //this would be enough (except for questions probably). Make nicer.
			model.addAttribute("locations", staticRepo.prettyLocations(u.getLocations()));
			model.addAttribute("languages", u.getLanguages());
			model.addAttribute("history", usersMatches(email));
			model.addAttribute("nrQ", real.getNumberOfRegularQuestions());
		}
		return "profile";
	}

	public List<HalfEdge> usersMatches(String email) {
		return userRepo.allMatchesForUser(email);
	}

}

