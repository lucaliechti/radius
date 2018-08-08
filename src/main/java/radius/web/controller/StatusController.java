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
import radius.UserForm;
import radius.data.JDBCUserRepository;

@Controller
@RequestMapping(value="/status")
public class StatusController {
	
	private JDBCUserRepository userRepo;
	
	//specify here which implementation of UserRepository will be used
	@Autowired
	public StatusController(JDBCUserRepository _userRepo) {
		this.userRepo = _userRepo;
	}
	
	@RequestMapping(method=GET)
	public String statusPage(@RequestParam(value = "login", required = false) String loggedin, Model model) {
		System.out.println("in the StatusController class");
		if(loggedin != null) {
			model.addAttribute("loggedin", "user has just logged in");
		}
		String email = SecurityContextHolder.getContext().getAuthentication().getName().toString();
		if(!userRepo.userHasAnswered(email)) {
			//user has not yet answered the questions
			List<String> lang = new ArrayList<String>();
			lang.add("DE");
			lang.add("FR");
			lang.add("IT");
			lang.add("EN");
			model.addAttribute("lang", lang);
			
			List<String> modus = new ArrayList<String>();
			modus.add("Single");
			modus.add("Pair");
			modus.add("Either");
			model.addAttribute("modi", modus);
			
			model.addAttribute("answerForm", new AnswerForm());
			return "answers";
		}
		else {
			//complex; grab matches etc.
		}

		return "status";
	}
	
}