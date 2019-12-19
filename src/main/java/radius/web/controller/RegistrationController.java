package radius.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import radius.data.dto.EmailDto;
import radius.data.form.UserForm;
import radius.web.components.CountrySpecificProperties;
import radius.web.components.ModelDecorator;
import radius.web.service.UserService;

import javax.validation.Valid;
import java.util.Locale;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class RegistrationController {
	
	private CountrySpecificProperties countryProperties;
	private ModelDecorator modelDecorator;
	private UserService userService;

	public RegistrationController(CountrySpecificProperties countryProperties, ModelDecorator modelDecorator,
								  UserService userService) {
		this.countryProperties = countryProperties;
		this.modelDecorator = modelDecorator;
		this.userService = userService;
	}

	@RequestMapping(value="/register", method=GET)
	public String reset(Model model) {
		model.addAllAttributes(modelDecorator.homeAttributes());
		return "home";
	}
	
	@RequestMapping(value="/register", method=POST)
	public String register(@ModelAttribute("registrationForm") @Valid UserForm registrationForm, BindingResult result,
						   Model model, Locale locale) {
		if(result.hasErrors()) {
			model.addAttribute("cantons", countryProperties.getCantons());
			model.addAttribute("newsletterForm", new EmailDto());
			return "home";
		}
		boolean success = userService.registerNewUserFromUserForm(registrationForm, locale);
		if(success) {
			model.addAttribute("waitForEmailConfirmation", Boolean.TRUE);
		} else {
			model.addAttribute("registrationError", true);
		}
		model.addAllAttributes(modelDecorator.homeAttributes());
		return "home";
	}

	@RequestMapping(value="/confirm", method=GET)
	public String confirm(@RequestParam(value="uuid") String uuid, Model model) {
		Optional<String> userEmail = userService.findEmailByUuid(uuid);
		if(!userEmail.isPresent()) {
			model.addAttribute("confirmation_error", true);
			model.addAllAttributes(modelDecorator.homeAttributes());
			return "home";
		}
		userService.enableUser(userEmail.get());
		model.addAttribute("emailconfirmed", true);
		model.addAllAttributes(modelDecorator.homeAttributes());
		return "home";
	}
}
