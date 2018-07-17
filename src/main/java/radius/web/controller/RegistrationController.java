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
import radius.data.JDBCCantonRepository;
import radius.data.JDBCUserRepository;
import radius.exceptions.EmailAlreadyExistsException;

@Controller
@RequestMapping(value="/register")
public class RegistrationController {
	
	private JDBCUserRepository userRepo;
	private JDBCCantonRepository cantonRepo;
	
	@Autowired
	PasswordEncoder encoder;
	
	//specify here which implementation of UserRepository will be used
	@Autowired
	public RegistrationController(JDBCUserRepository _userRepo, JDBCCantonRepository _cantonRepo) {
		this.userRepo = _userRepo;
		this.cantonRepo = _cantonRepo;
	}

	@RequestMapping(method=GET)
	public String registrationPage(Model model) {
		model.addAttribute("registrationForm", new UserForm());
		model.addAttribute("cantons", cantonRepo.allCantons());
		return "register";
	}
	
	@RequestMapping(method=POST)
	public String register(@ModelAttribute("registrationForm") @Valid UserForm registrationForm, BindingResult result, Model model) {
		if(result.hasErrors()) {
			System.out.println("RegistrationController: Error registering");
			return "register";
		}
		String firstName = registrationForm.getFirstName();
		String lastName = registrationForm.getLastName();
		String canton = registrationForm.getCanton();
		String email = registrationForm.getEmail();
		String password = registrationForm.getPassword();
		User user = new User(firstName, lastName, canton, email, encoder.encode(password));

		try {
			System.out.println("saving user...");
			userRepo.saveUser(user);
			System.out.println("success");
		}
		catch (EmailAlreadyExistsException eaee) {
			System.out.println(eaee.getMessage());
			model.addAttribute("emailExistsError", new Boolean(true));
			return "register";
		}
		catch (Exception e) {
			System.out.println("RegistrationController: error saving new user!");
			e.printStackTrace();
			model.addAttribute("registrationError", new Boolean(true));
			return "register";
		}
		model.addAttribute("waitForEmailConfirmation", new Boolean(true));
		return "login";
	}
}