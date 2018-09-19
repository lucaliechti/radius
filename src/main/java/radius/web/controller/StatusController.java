package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import radius.MeetingFeedbackForm;
import radius.User;
//import radius.AnswerForm;
import radius.data.JDBCStaticResourceRepository;
import radius.data.JDBCUserRepository;

@Controller
@RequestMapping(value="/status")
public class StatusController {
	
	private JDBCUserRepository userRepo;
//	private JDBCStaticResourceRepository staticRepo;
	private AnswerController ac;
	
	//specify here which implementation of UserRepository will be used
	@Autowired
	public StatusController(JDBCUserRepository _userRepo, JDBCStaticResourceRepository _staticRepo, AnswerController _ac) {
		this.userRepo = _userRepo;
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
			model.addAttribute("feedbackForm", new MeetingFeedbackForm());
		}
		return "status";
	}
}