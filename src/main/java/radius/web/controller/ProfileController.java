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
import radius.User;
import radius.data.JDBCUserRepository;

@Controller
@RequestMapping(value="/profile")
public class ProfileController {
	
	private JDBCUserRepository userRepo;
	
	@Autowired
	public ProfileController(JDBCUserRepository _userRepo) {
		this.userRepo = _userRepo;
	}

	@RequestMapping(method=GET)
	public String profile(@RequestParam(value = "login", required = false) String loggedin, Model model) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName().toString();
		if(!userRepo.userHasAnswered(email)) {
			//user has not yet answered the questions
			model.addAttribute("noAnswers", true);
		}
		else {
			User u = userRepo.findUserByEmail(email);
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
			model.addAttribute("languages", u.getLanguages());
			model.addAttribute("locations", u.getLocations());			
		}
		return "profile";
	}
}

