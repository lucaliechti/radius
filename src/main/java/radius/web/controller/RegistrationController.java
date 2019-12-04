package radius.web.controller;

import org.springframework.context.MessageSource;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import radius.data.dto.EmailDto;
import radius.User;
import radius.data.form.UserForm;
import radius.data.repository.*;
import radius.exceptions.EmailAlreadyExistsException;
import radius.web.components.EmailService;
import radius.web.components.ModelRepository;
import radius.web.components.ProfileDependentProperties;

import javax.validation.Valid;
import java.util.Locale;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class RegistrationController {
	
	private UserRepository userRepo;
	private StaticResourceRepository staticResourceRepo;
	private EmailService emailService;
	private JavaMailSenderImpl helloMailSender;
	private ModelRepository modelRepository;
    private MessageSource messageSource;
	private PasswordEncoder encoder;
	private ProfileDependentProperties prop;

	public RegistrationController(JDBCUserRepository _userRepo, JSONStaticResourceRepository _staticRepo,
								  EmailService _ses, JavaMailSenderImpl helloMailSender, ModelRepository modelRepository,
								  MessageSource messageSource, PasswordEncoder encoder, ProfileDependentProperties prop) {
		this.userRepo = _userRepo;
		this.staticResourceRepo = _staticRepo;
		this.emailService = _ses;
		this.helloMailSender = helloMailSender;
		this.modelRepository = modelRepository;
		this.messageSource = messageSource;
		this.encoder = encoder;
		this.prop = prop;
	}

	@RequestMapping(value="/register", method=GET)
	public String reset(Model model) {
		model.addAllAttributes(modelRepository.homeAttributes());
		return "home";
	}
	
	@RequestMapping(value="/register", method=POST)
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

	@RequestMapping(value="/confirm", method=GET)
	public String confirm(@RequestParam(value="uuid") String uuid, Model model) {
		String userEmail;
		try {
			userEmail = userRepo.findEmailByUuid(uuid);
		} catch (IncorrectResultSizeDataAccessException irsdae) {
			model.addAttribute("confirmation_error", true);
			model.addAllAttributes(modelRepository.homeAttributes());
			return "home";
		}
		userRepo.enableUser(userEmail);
		model.addAttribute("emailconfirmed", true);
		model.addAllAttributes(modelRepository.homeAttributes());
		return "home";
	}

	public String cleanlyRegisterNewUser(Model model, Locale locale, String firstName, String lastName, String canton,
								  String email, String password) {
		User user = new User(firstName, lastName, canton, email, encoder.encode(password));
		try {
			userRepo.saveUser(user);
		} catch (EmailAlreadyExistsException eaee) {
			model.addAttribute("emailExistsError", Boolean.TRUE);
			model.addAllAttributes(modelRepository.homeAttributes());
			return "home";
		} catch (Exception e) {
			model.addAttribute("registrationError", true);
			model.addAllAttributes(modelRepository.homeAttributes());
			return "home";
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
			model.addAllAttributes(modelRepository.homeAttributes());
			return "home";
		}
		model.addAttribute("waitForEmailConfirmation", Boolean.TRUE);
		model.addAllAttributes(modelRepository.homeAttributes());
		return "home";
	}
}
