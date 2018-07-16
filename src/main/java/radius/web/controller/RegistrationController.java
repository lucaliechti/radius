package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import radius.User;
import radius.UserForm;
import radius.data.JDBCUserRepository;

@Controller
@RequestMapping(value="/register")
public class RegistrationController {
	
	private JDBCUserRepository userRepo;
	
	@Autowired
	PasswordEncoder encoder;
	
	//specify here which implementation of UserRepository will be used
	@Autowired
	public RegistrationController(JDBCUserRepository _repo) {
		this.userRepo = _repo;
	}

	@RequestMapping(method=GET)
	public String registrationPage(Model model) {
		model.addAttribute("registrationForm", new UserForm());
		return "register";
	}
	
	@RequestMapping(method=POST)
	public String register(@ModelAttribute("registrationForm") @Valid UserForm registrationForm, BindingResult result, Model model) {
		if(result.hasErrors()) {
			System.out.println("RegistrationController: Error registering");
			return "register";
		}
		String email = registrationForm.getEmail();
		String password = registrationForm.getPassword();
		User user = new User(email, encoder.encode(password));
		
		//TODO: Do this much nicer
		try {
			System.out.println("saving user...");
			userRepo.saveUser(user);
			System.out.println("success");
		}
		catch (Exception e) {
			System.out.println("RegistrationController: error saving new user!!");
			e.printStackTrace();
		}
		return "login";
	}
}