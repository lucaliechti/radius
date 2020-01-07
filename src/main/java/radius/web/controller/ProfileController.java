package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
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
import radius.web.service.AnswerService;
import radius.web.service.ConfigService;
import radius.web.service.MatchingService;
import radius.web.service.UserService;

@Controller
@RequestMapping(value="/profile")
public class ProfileController {

	private UserService userService;
	private AnswerService answerService;
	private MatchingService matchingService;
	private CountrySpecificProperties countryProperties;
	private ConfigService configService;

	@Autowired
	public ProfileController(UserService userService, CountrySpecificProperties countryProperties,
							 ConfigService configService, MatchingService matchingService, AnswerService answerService) {
		this.userService = userService;
		this.answerService = answerService;
		this.countryProperties = countryProperties;
		this.configService = configService;
		this.matchingService = matchingService;
	}

	@RequestMapping(method=GET)
	public String profile(@RequestParam(value="login", required=false) String loggedin, Model model, Locale locale) {
		if(loggedin != null) {
			model.addAttribute("loggedin", "user has just logged in");
		}
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userService.findUserByEmail(email).get();
		if(!user.isEnabled()) {
			model.addAttribute("not_enabled", true);
			return "home";
		} else if(!answerService.userHasValidlyAnswered(user)) {
			model.addAttribute("lang", countryProperties.getLanguages());
			model.addAttribute("answerForm", new AnswerForm());
			return "answers";
		} else {
			model.addAttribute("user", user);
			List<String> answers = user.getRegularanswers().stream().map(User::convertAnswerToString).
					filter(Objects::nonNull).map(String::toLowerCase).collect(Collectors.toList());
			model.addAttribute("answers", answers);
			if(configService.specialActive()) {
				List<String> specialanswers = user.getSpecialanswers().stream().map(User::convertAnswerToString).
						filter(Objects::nonNull).map(String::toLowerCase).collect(Collectors.toList());
				model.addAttribute("specialanswers", specialanswers);
				model.addAttribute("specialquestions", configService.specialQuestions(locale));
			}
			model.addAttribute("locations", countryProperties.prettyLocations(user.getLocations()));
			model.addAttribute("history", matchingService.allMatchesForUser(email));
			model.addAttribute("nrQ", configService.numberOfRegularQuestions());
			model.addAttribute("nrSQ", configService.numberOfVotes());
			model.addAttribute("vote", configService.currentVote());
			model.addAttribute("regularquestions", configService.regularQuestions(locale));
		}
		return "profile";
	}
}
