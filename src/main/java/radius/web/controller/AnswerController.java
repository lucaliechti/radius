package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import radius.data.form.AnswerForm;
import radius.User;
import radius.data.JDBCStaticResourceRepository;
import radius.data.JDBCUserRepository;
import radius.web.components.RealWorldConfiguration.RealWorldProperties;

@Controller
@RequestMapping(value="/answers")
public class AnswerController {
	
	private JDBCUserRepository userRepo;
	private JDBCStaticResourceRepository staticRepo;
	private RealWorldProperties realWorld;
	private boolean specialIsActive;
	private StatusController sc;
	private MessageSource validationMessages;

	private static final String ERROR_ANSWER_REGULAR_QUESTIONS = "error.answerRegularQuestions";
	private static final String ERROR_AT_LEAST_ONE_SET = "error.answerOneSetOfQuestions";

	@Autowired
	public AnswerController(JDBCUserRepository _userRepo, JDBCStaticResourceRepository _staticRepo,
							RealWorldProperties _realWorld, MessageSource validationMessageSource) {
		this.userRepo = _userRepo;
		this.staticRepo = _staticRepo;
		this.realWorld = _realWorld;
		this.specialIsActive = realWorld.isSpecialIsActive();
		this.validationMessages = validationMessageSource;
	}

	@Autowired
	public void setStatusController(StatusController sc) {
		this.sc = sc;
	}

	@RequestMapping(method=GET)
	public String answer(Model model) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		User u = userRepo.findUserByEmail(email);
		if(!u.getAnswered()) {
			model.addAttribute("newUser", true);
			model.addAttribute("answerForm", new AnswerForm());
		}
		else {
			AnswerForm f = newFormFromUser(u);
			model.addAttribute("answerForm", f);
		}
		return "answers";
	}
	
	@RequestMapping(method=POST)
	public String answer(@Valid @ModelAttribute("answerForm") AnswerForm answerForm,
						 BindingResult result, Model model, Locale locale)  {
		if (!validlyAnswered(answerForm)) {
			provideFeedback(answerForm, result, locale);
			return "answers";
		}
		else if(result.hasErrors()) {
			return "answers";
		}
		else {
			String email = SecurityContextHolder.getContext().getAuthentication().getName();
			User u = userRepo.findUserByEmail(email);
			u = updateUserFromForm(u, answerForm);
			u.setAnsweredRegular(true);
			u.setStatus("WAITING");
			userRepo.updateUser(u);
			if (answerForm.getSpecialanswers() != null) {
				userRepo.updateVotes(email, realWorld.getCurrentVote(),
						answerForm.getSpecialanswers().stream().map(User::convertAnswer).collect(Collectors.toList()));
			}
			return sc.statusPage(model, locale);
		}
	}

	@ModelAttribute
	public void prepare(Model model) {
		model.addAttribute("lang", staticRepo.languages());
		model.addAttribute("modi", staticRepo.modi());
		model.addAttribute("nrQ", realWorld.getNumberOfRegularQuestions());
		model.addAttribute("special", realWorld.isSpecialIsActive());
		model.addAttribute("nrV", realWorld.getNumberOfVotes());
		model.addAttribute("currentVote", realWorld.getCurrentVote());
	}
	
	private AnswerForm newFormFromUser(User u) {
		AnswerForm form = new AnswerForm();
		form.setMotivation(u.getMotivation());
		form.setModus(u.getModusAsString());
		form.setLanguages(u.getLanguages());
		form.setLocations(User.createLocString(u.getLocations()));
		form.setRegularanswers(u.getRegularAnswersAsListOfStrings());
		form.setSpecialanswers(u.getSpecialAnswersAsListOfStrings());
		return form;
	}

	private User updateUserFromForm(User user, AnswerForm answerForm) {
		user.setLanguages(answerForm.getLanguages());
		user.setModus(answerForm.getModus());
		user.setMotivation(answerForm.getMotivation().length() == 0 ? null : answerForm.getMotivation());
		user.setRegularanswers(answerForm.getRegularanswers());
		user.setSpecialanswers(answerForm.getSpecialanswers());
		try {
			user.setLocations(Arrays.stream(
				answerForm.getLocations().split(";")).map(Integer::valueOf).collect(Collectors.toList())
			);
		}
		catch (NumberFormatException nfe) {
			System.out.print("Answercontroller: "); nfe.printStackTrace();
		}
		return user;
	}

	private boolean validlyAnswered(AnswerForm form) {
		return validAnswers(form.getRegularanswers(), realWorld.getNumberOfRegularQuestions())
				|| validAnswers(form.getSpecialanswers(), realWorld.getNumberOfVotes());
	}

	private boolean validAnswers(List<String> answers, int nrAnswers) {
		return answers.size() == nrAnswers && (answers.contains("TRUE") || answers.contains("FALSE"));
	}

	private void provideFeedback (AnswerForm form, BindingResult result, Locale locale) {
		if(!specialIsActive) {
			addFieldError(result, "answerForm", "regularanswers", ERROR_ANSWER_REGULAR_QUESTIONS, locale);
		} else {
			addFieldError(result, "answerForm", "regularanswers", ERROR_AT_LEAST_ONE_SET, locale);
			addFieldError(result, "answerForm", "specialanswers", ERROR_AT_LEAST_ONE_SET, locale);
		}
	}

	private void addFieldError(BindingResult result, String form, String field, String message, Locale loc) {
		FieldError regular = new FieldError(form, field, validationMessages.getMessage(message, new String[]{}, loc));
		result.addError(regular);
	}
}
