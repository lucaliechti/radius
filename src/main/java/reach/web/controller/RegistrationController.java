package reach.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import reach.User;
import reach.data.JDBCUserRepository;

@Controller
@RequestMapping(value="/register")
public class RegistrationController {
	
	private JDBCUserRepository repo;
	
	//specify here which implementation of UserRepository will be used
	@Autowired
	public RegistrationController(JDBCUserRepository _repo) {
		this.repo = _repo;
	}

	@RequestMapping(method=GET)
	public String registrationPage(Model model) {
		System.out.println("in the RegistrationController class");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("username", username);
		return "register";
	}
	
	@RequestMapping(method=POST)
	public String register(@ModelAttribute("registrationForm") User registrationForm, Model model) {
		String newusername = registrationForm.getUsername();
		String password = registrationForm.getPassword();
		User user = new User(newusername, password);
		
		//TODO: Do this much nicer
		try {
			System.out.println("saving user...");
			repo.saveUser(user);
			System.out.println("success");
		}
		catch (Exception e) {
			System.out.println("error!!");
		}
		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("username", username);
		System.out.println("Logged in as user " + username);
		return "login";
	}
}