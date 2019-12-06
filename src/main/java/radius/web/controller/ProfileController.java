package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import radius.data.form.AnswerForm;
import radius.User;
import radius.web.components.CountrySpecificProperties;
import radius.web.components.RealWorldProperties;
import radius.web.service.MatchingService;
import radius.web.service.UserService;

@Controller
@RequestMapping(value="/profile")
public class ProfileController {

	private UserService userService;
	private MatchingService matchingService;
	private CountrySpecificProperties countryProperties;
	private RealWorldProperties real;

	@Autowired
	public ProfileController(UserService userService, CountrySpecificProperties countryProperties,
							 RealWorldProperties real, MatchingService matchingService) {
		this.userService = userService;
		this.countryProperties = countryProperties;
		this.real = real;
		this.matchingService = matchingService;
	}

	@RequestMapping(method=GET)
	public String profile(@RequestParam(value="login", required=false) String loggedin, Model model) {
		if(loggedin != null) {
			model.addAttribute("loggedin", "user has just logged in");
		}
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userService.findUserByEmail(email).get();
		if(!user.isEnabled()) {
			model.addAttribute("not_enabled", true);
			return "home";
		} else if(!user.isAnsweredRegular()) {
			model.addAttribute("lang", countryProperties.getLanguages());
			model.addAttribute("answerForm", new AnswerForm());
			return "answers";
		} else {
			model.addAttribute("user", user);
			List<String> answers = user.getRegularAnswersAsListOfStrings().stream().map(String::toLowerCase)
					.collect(Collectors.toList());
			model.addAttribute("answers", answers);
			model.addAttribute("locations", countryProperties.prettyLocations(user.getLocations()));
			model.addAttribute("history", matchingService.allMatchesForUser(email));
			model.addAttribute("nrQ", real.getNumberOfRegularQuestions());
		}
		return "profile";
	}
}
