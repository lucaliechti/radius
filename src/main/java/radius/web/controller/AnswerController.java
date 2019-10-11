package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
	
	@Autowired
	private StatusController sc;

	@Autowired
	private RealWorldProperties realWorld;
	
	@Autowired
	public AnswerController(JDBCUserRepository _userRepo, JDBCStaticResourceRepository _staticRepo) {
		this.userRepo = _userRepo;
		this.staticRepo = _staticRepo;
	}

	@RequestMapping(method=GET)
	public String answer(Model model) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		User u = userRepo.findUserByEmail(email);
		if(!u.getAnswered()) { //TODO: Do this directly on the user object
			model.addAttribute("newUser", true);
			model.addAttribute("answerForm", new AnswerForm());
		}
		else {
			AnswerForm f = newFormFromUser(u);
			model.addAttribute("answerForm", f);
		}
		prepare(model);
		return "answers";
	}
	
	@RequestMapping(method=POST)
	public String answer(@Valid @ModelAttribute("answerForm") AnswerForm answerForm, BindingResult result, Model model, Locale locale)  {
		if(result.hasErrors()) {
			prepare(model);
			return "answers";
		}
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		User u = userRepo.findUserByEmail(email);
		u = updateUserFromForm(u, answerForm);
		u.setAnsweredRegular(true);
		u.setStatus("WAITING"); //new
		userRepo.updateUser(u);
		if(answerForm.getSpecialanswers() != null) {
			userRepo.updateVotes(email, realWorld.getCurrentVote(), answerForm.getSpecialanswers().stream().map(User::convertAnswer).collect(Collectors.toList()));
		}
		return sc.statusPage(model, locale);
	}
	
	private void prepare(Model model) {
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
		try { user.setLocations(Arrays.asList(answerForm.getLocations().split(";")).stream().map(Integer::valueOf).collect(Collectors.toList())); }
		catch (NumberFormatException nfe) { System.out.print("Answercontroller: "); nfe.printStackTrace(); }
		return user;
	}
}
