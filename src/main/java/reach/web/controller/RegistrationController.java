package reach.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import reach.User;
import reach.data.JDBCUserRepository;

@Controller
@RequestMapping(value="/register")
public class RegistrationController {
	
	private JDBCUserRepository userRepo;
	
	//specify here which implementation of UserRepository will be used
	@Autowired
	public RegistrationController(JDBCUserRepository _repo) {
		this.userRepo = _repo;
	}

	@RequestMapping(method=GET)
	public String registrationPage(Model model) {
		System.out.println("in the RegistrationController class");
		return "register";
	}
	
	@RequestMapping(method=POST)
	public String register(@ModelAttribute("registrationForm") User registrationForm, Model model) {
		String email = registrationForm.getEmail();
		String password = registrationForm.getPassword();
		User user = new User(email, password);
		
		//TODO: Do this much nicer
		try {
			System.out.println("saving user...");
			userRepo.saveUser(user);
			System.out.println("success");
		}
		catch (Exception e) {
			System.out.println("error!!");
			e.printStackTrace();
		}
		return "login";
	}
}