package radius.web.controller;

//import static org.springframework.web.bind.annotation.RequestMethod.GET;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import radius.User;
import radius.UserForm;
import radius.data.*;
import radius.exceptions.EmailAlreadyExistsException;
import radius.web.components.EmailService;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(value="/register")
public class RegistrationController {
	
	private UserRepository userRepo;
	private StaticResourceRepository staticResourceRepo; //leftover from when this was an own page
	private UUIDRepository uuidRepo;
	private EmailService emailService;
	
    @Autowired
    private MessageSource messageSource;
	
	@Autowired
	PasswordEncoder encoder;
	
	//specify here which implementation of UserRepository will be used
	@Autowired
	public RegistrationController(JDBCUserRepository _userRepo, JDBCStaticResourceRepository _staticRepo, EmailService _ses, JDBCUUIDRepository uuid) {
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
	public String register(@ModelAttribute("registrationForm") @Valid UserForm registrationForm, BindingResult result, Model model, Locale locale) throws UnsupportedEncodingException {
		//all kinds of errors
		if(result.hasErrors()) {
			System.out.println("RegistrationController: Error registering");
			model.addAttribute("cantons", staticResourceRepo.cantons());
			return "home";
		}
		String firstName = new String(registrationForm.getFirstName().getBytes("ISO-8859-1"), "UTF-8");
		String lastName = new String(registrationForm.getLastName().getBytes("ISO-8859-1"), "UTF-8");
		String canton = registrationForm.getCanton();
		String email = new String(registrationForm.getEmail().getBytes("ISO-8859-1"), "UTF-8");
		String password = new String(registrationForm.getPassword().getBytes("ISO-8859-1"), "UTF-8");
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
			emailService.sendSimpleMessage("hello", email, messageSource.getMessage("email.confirm.title", new Object[]{}, locale), messageSource.getMessage("email.confirm.content", new Object[]{firstName, lastName, "https://radius-schweiz.ch/confirm?uuid=" + uuid.toString()}, locale));
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