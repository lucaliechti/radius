package radius.web.controller;

//import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.UUID;

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
import radius.data.JDBCStaticResourceRepository;
import radius.data.JDBCUUIDRepository;
import radius.data.JDBCUserRepository;
import radius.data.StaticResourceRepository;
import radius.data.UUIDRepository;
import radius.data.UserRepository;
import radius.exceptions.EmailAlreadyExistsException;
import radius.web.components.EmailService;
import radius.web.components.SimpleEmailService;

@Controller
@RequestMapping(value="/register")
public class RegistrationController {
	
	private UserRepository userRepo;
	private StaticResourceRepository staticResourceRepo; //leftover from when this was an own page
	private UUIDRepository uuidRepo;
	private EmailService emailService;
	
	@Autowired
	PasswordEncoder encoder;
	
	//specify here which implementation of UserRepository will be used
	@Autowired
	public RegistrationController(JDBCUserRepository _userRepo, JDBCStaticResourceRepository _staticRepo, SimpleEmailService _ses, JDBCUUIDRepository uuid) {
		this.userRepo = _userRepo;
		this.staticResourceRepo = _staticRepo;
		this.emailService = _ses;
		this.uuidRepo = uuid;
	}
	
	/* LL: now moved to home page. this cannot be GETed.
	@RequestMapping(method=GET)
	public String registrationPage(Model model) {
		model.addAttribute("registrationForm", new UserForm());
		model.addAttribute("cantons", staticResourceRepo.cantons());
		return "register";
	}*/
	
	@RequestMapping(method=POST)
	public String register(@ModelAttribute("registrationForm") @Valid UserForm registrationForm, BindingResult result, Model model) {
		//all kinds of errors
		if(result.hasErrors()) {
			System.out.println("RegistrationController: Error registering");
			model.addAttribute("cantons", staticResourceRepo.cantons());
			return "home";
		}
		String firstName = registrationForm.getFirstName();
		String lastName = registrationForm.getLastName();
		String canton = registrationForm.getCanton();
		String email = registrationForm.getEmail();
		String password = registrationForm.getPassword();
		User user = new User(firstName, lastName, canton, email, encoder.encode(password));
		UUID uuid = UUID.randomUUID();
		try {
			System.out.println("saving user...");
			userRepo.saveUser(user);
			uuidRepo.insertUUID(email, uuid.toString());
			System.out.println("success");
		}
		catch (EmailAlreadyExistsException eaee) {
			System.out.println(eaee.getMessage());
			model.addAttribute("emailExistsError", new Boolean(true));
			model.addAttribute("registrationForm", new UserForm());
			model.addAttribute("cantons", staticResourceRepo.cantons());
			return "home";
		}
		catch (Exception e) {
			System.out.println("RegistrationController: error saving new user!");
			e.printStackTrace();
			model.addAttribute("registrationError", true);
			model.addAttribute("registrationForm", new UserForm());
			model.addAttribute("cantons", staticResourceRepo.cantons());
			return "home";
		}
		System.out.println(uuidRepo.findUserByUUID(uuid.toString()));
		
		try {
			emailService.sendSimpleMessage("hello", email, "willkommen!", "willkommen bei Radius! Gehen Sie mal auf https://radius-schweiz.ch/confirm?uuid=" + uuid.toString() + ", dann sind Sie registriert.");
		}
		catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("registrationError", true);
		}
			
		//success
		model.addAttribute("waitForEmailConfirmation", new Boolean(true));
		model.addAttribute("registrationForm", new UserForm());
		model.addAttribute("cantons", staticResourceRepo.cantons());
		return "home";
	}
}