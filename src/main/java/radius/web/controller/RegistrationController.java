package radius.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import radius.data.dto.EmailDto;
import radius.data.form.UserForm;
import radius.data.repository.JSONStaticResourceRepository;
import radius.data.repository.StaticResourceRepository;
import radius.web.components.ModelRepository;
import radius.web.service.UserService;

import javax.validation.Valid;
import java.util.Locale;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class RegistrationController {
	
	private StaticResourceRepository staticResourceRepo;
	private ModelRepository modelRepository;
	private UserService userService;

	public RegistrationController(JSONStaticResourceRepository staticRepo, ModelRepository modelRepository,
								  UserService userService) {
		this.staticResourceRepo = staticRepo;
		this.modelRepository = modelRepository;
		this.userService = userService;
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
		boolean success = userService.registerNewUserFromUserForm(registrationForm, locale);
		if(success) {
			model.addAttribute("waitForEmailConfirmation", Boolean.TRUE);
		} else {
			model.addAttribute("registrationError", true);
		}
		model.addAllAttributes(modelRepository.homeAttributes());
		return "home";
	}

	@RequestMapping(value="/confirm", method=GET)
	public String confirm(@RequestParam(value="uuid") String uuid, Model model) {
		Optional<String> userEmail = userService.findEmailByUuid(uuid);
		if(userEmail.isEmpty()) {
			model.addAttribute("confirmation_error", true);
			model.addAllAttributes(modelRepository.homeAttributes());
			return "home";
		}
		userService.enableUser(userEmail.get());
		model.addAttribute("emailconfirmed", true);
		model.addAllAttributes(modelRepository.homeAttributes());
		return "home";
	}
}
