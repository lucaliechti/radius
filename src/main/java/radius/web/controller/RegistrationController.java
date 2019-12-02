package radius.web.controller;

import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import radius.data.dto.EmailDto;
import radius.User;
import radius.data.form.UserForm;
import radius.data.repository.JDBCStaticResourceRepository;
import radius.data.repository.JDBCUserRepository;
import radius.data.repository.StaticResourceRepository;
import radius.data.repository.UserRepository;
import radius.exceptions.EmailAlreadyExistsException;
import radius.web.components.EmailService;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(value="/register")
public class RegistrationController {
	
	private UserRepository userRepo;
	private StaticResourceRepository staticResourceRepo;
	private EmailService emailService;
	private JavaMailSenderImpl helloMailSender;
	private HomeController h;
    private MessageSource messageSource;
	private PasswordEncoder encoder;

	public RegistrationController(JDBCUserRepository _userRepo, JDBCStaticResourceRepository _staticRepo,
								  EmailService _ses, JavaMailSenderImpl helloMailSender, HomeController h, MessageSource messageSource,
								  PasswordEncoder encoder) {
		this.userRepo = _userRepo;
		this.staticResourceRepo = _staticRepo;
		this.emailService = _ses;
		this.helloMailSender = helloMailSender;
		this.h = h;
		this.messageSource = messageSource;
		this.encoder = encoder;
	}

	@RequestMapping(method=GET)
	public String reset(Model model) {
		return h.cleanlyHome(model);
	}
	
	@RequestMapping(method=POST)
	public String register(@ModelAttribute("registrationForm") @Valid UserForm registrationForm, BindingResult result, Model model, Locale locale) throws UnsupportedEncodingException {
		if(result.hasErrors()) {
			System.out.println("RegistrationController: Error registering");
			model.addAttribute("cantons", staticResourceRepo.cantons());
			model.addAttribute("newsletterForm", new EmailDto());
			return "home";
		}
		String firstName = registrationForm.getFirstName();
		String lastName = registrationForm.getLastName();
		String canton = registrationForm.getCanton().equals("NONE") ? null : registrationForm.getCanton();
		String email = registrationForm.getEmail();
		String password = registrationForm.getPassword();

		return cleanlyRegisterNewUser(model, locale, firstName, lastName, canton, email, password);
	}

	String cleanlyRegisterNewUser(Model model, Locale locale, String firstName, String lastName, String canton, String email, String password) {
		User user = new User(firstName, lastName, canton, email, encoder.encode(password));
		try {
			userRepo.saveUser(user);
		}
		catch (EmailAlreadyExistsException eaee) {
			System.out.println(eaee.getMessage());
			model.addAttribute("emailExistsError", Boolean.TRUE);
			return h.cleanlyHome(model);
		}
		catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("registrationError", true);
			return h.cleanlyHome(model);
		}
		System.out.println("email confirmation uuid: " + user.getUuid());
		try {
			emailService.sendSimpleMessage(
					email,
					messageSource.getMessage("email.confirm.title", new Object[]{}, locale),
					messageSource.getMessage("email.confirm.content", new Object[]{firstName, lastName,
							"https://radius-schweiz.ch/confirm?uuid=" + user.getUuid()}, locale),
					helloMailSender
			);
		}
		catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("registrationError", true);
			return h.cleanlyHome(model);
		}
		model.addAttribute("waitForEmailConfirmation", Boolean.TRUE);
		return h.cleanlyHome(model);
	}
}
