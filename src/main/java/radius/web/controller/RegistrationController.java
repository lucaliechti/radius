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
import radius.data.repository.*;
import radius.exceptions.EmailAlreadyExistsException;
import radius.web.components.EmailService;
import radius.web.components.ProfileDependentProperties;

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
	private ProfileDependentProperties prop;

	public RegistrationController(JDBCUserRepository _userRepo, JSONStaticResourceRepository _staticRepo,
								  EmailService _ses, JavaMailSenderImpl helloMailSender, HomeController h,
								  MessageSource messageSource, PasswordEncoder encoder, ProfileDependentProperties prop) {
		this.userRepo = _userRepo;
		this.staticResourceRepo = _staticRepo;
		this.emailService = _ses;
		this.helloMailSender = helloMailSender;
		this.h = h;
		this.messageSource = messageSource;
		this.encoder = encoder;
		this.prop = prop;
	}

	@RequestMapping(method=GET)
	public String reset(Model model) {
		return h.cleanlyHome(model);
	}
	
	@RequestMapping(method=POST)
	public String register(@ModelAttribute("registrationForm") @Valid UserForm registrationForm, BindingResult result,
						   Model model, Locale locale) {
		if(result.hasErrors()) {
			model.addAttribute("cantons", staticResourceRepo.cantons());
			model.addAttribute("newsletterForm", new EmailDto());
			return "home";
		}
		String firstName = registrationForm.getFirstName();
		String lastName = registrationForm.getLastName();
		String canton = registrationForm.getCanton();
		String email = registrationForm.getEmail();
		String password = registrationForm.getPassword();

		return cleanlyRegisterNewUser(model, locale, firstName, lastName, canton, email, password);
	}

	public String cleanlyRegisterNewUser(Model model, Locale locale, String firstName, String lastName, String canton,
								  String email, String password) {
		User user = new User(firstName, lastName, canton, email, encoder.encode(password));
		try {
			userRepo.saveUser(user);
		} catch (EmailAlreadyExistsException eaee) {
			model.addAttribute("emailExistsError", Boolean.TRUE);
			return h.cleanlyHome(model);
		} catch (Exception e) {
			model.addAttribute("registrationError", true);
			return h.cleanlyHome(model);
		}
		try {
			emailService.sendSimpleMessage(
					email,
					messageSource.getMessage("email.confirm.title", new Object[]{}, locale),
					messageSource.getMessage("email.confirm.content", new Object[]{firstName, lastName,
							prop.getUrl() + "/confirm?uuid=" + user.getUuid()}, locale),
					helloMailSender
			);
		} catch (Exception e) {
			model.addAttribute("registrationError", true);
			return h.cleanlyHome(model);
		}
		model.addAttribute("waitForEmailConfirmation", Boolean.TRUE);
		return h.cleanlyHome(model);
	}
}
