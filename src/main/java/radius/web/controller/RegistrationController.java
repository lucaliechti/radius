package radius.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import radius.data.form.EmailForm;
import radius.User;
import radius.data.form.UserForm;
import radius.data.*;
import radius.exceptions.EmailAlreadyExistsException;
import radius.web.components.EmailService;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(value="/register")
public class RegistrationController {
	
	private UserRepository userRepo;
	private StaticResourceRepository staticResourceRepo; //leftover from when this was an own page
	private UUIDRepository uuidRepo;
	
	@Qualifier("helloMailSender")
	@Autowired
	private JavaMailSenderImpl helloMailSender;

	@Autowired
	private HomeController h;
	
	private EmailService emailService;
	
    @Autowired
    private MessageSource messageSource;
	
	@Autowired
	private PasswordEncoder encoder;
	
	//specify here which implementation of UserRepository will be used
	@Autowired
	public RegistrationController(JDBCUserRepository _userRepo, JDBCStaticResourceRepository _staticRepo, EmailService _ses, JDBCUUIDRepository uuid) {
		this.userRepo = _userRepo;
		this.staticResourceRepo = _staticRepo;
		this.emailService = _ses;
		this.uuidRepo = uuid;
	}

	@RequestMapping(method=GET)
	public String reset(Model model) {
		return h.cleanlyHome(model);
	}
	
	@RequestMapping(method=POST)
	public String register(@ModelAttribute("registrationForm") @Valid UserForm registrationForm, BindingResult result, Model model, Locale locale) throws UnsupportedEncodingException {
		//all kinds of errors
		if(result.hasErrors()) {
			System.out.println("RegistrationController: Error registering");
			model.addAttribute("cantons", staticResourceRepo.cantons());
			model.addAttribute("newsletterForm", new EmailForm());
			return "home"; //here we cannot return cleanly, because there registrationForm has been in use
		}
		String firstName = new String(registrationForm.getFirstName().getBytes("ISO-8859-1"), "UTF-8");
		String lastName = new String(registrationForm.getLastName().getBytes("ISO-8859-1"), "UTF-8");
		String canton = registrationForm.getCanton();
		String email = new String(registrationForm.getEmail().getBytes("ISO-8859-1"), "UTF-8");
		String password = new String(registrationForm.getPassword().getBytes("ISO-8859-1"), "UTF-8");

		return cleanlyRegisterNewUser(model, locale, firstName, lastName, canton, email, password);
	}

	protected String cleanlyRegisterNewUser(Model model, Locale locale, String firstName, String lastName, String canton, String email, String password) {
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
			model.addAttribute("emailExistsError", Boolean.TRUE);
			return h.cleanlyHome(model);
		}
		catch (Exception e) {
			System.out.println("RegistrationController: error saving new user!");
			e.printStackTrace();
			model.addAttribute("registrationError", true);
			return h.cleanlyHome(model);
		}
		System.out.println("email confirmation uuid: " + uuidRepo.findUserByUUID(uuid.toString()));

		try {
			emailService.sendSimpleMessage(
					email,
					messageSource.getMessage("email.confirm.title", new Object[]{}, locale),
					messageSource.getMessage("email.confirm.content", new Object[]{firstName, lastName, "https://radius-schweiz.ch/confirm?uuid=" + uuid.toString()}, locale),
					helloMailSender
			);
		}
		catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("registrationError", true);
			return h.cleanlyHome(model);
		}

		//success
		model.addAttribute("waitForEmailConfirmation", Boolean.TRUE);
		return h.cleanlyHome(model);
	}
}