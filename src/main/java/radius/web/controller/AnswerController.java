package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import radius.AnswerForm;
import radius.User;
import radius.data.JDBCUserRepository;

@Controller
@RequestMapping(value="/answers")
public class AnswerController {
	
	private JDBCUserRepository userRepo;
	
	@Autowired
	public AnswerController(JDBCUserRepository _userRepo) {
		this.userRepo = _userRepo;
	}

	@RequestMapping(method=GET)
	public String answer(Model model) {
		System.out.println("in the AnswerController class");
		addListsTo(model);
		model.addAttribute("answerForm", new AnswerForm());
		return "answers";
	}
	
	@RequestMapping(method=POST)
	public String answer(@Valid @ModelAttribute("answerForm") AnswerForm answerForm, BindingResult result, Model model) {
		if(result.hasErrors()) {
			addListsTo(model);
			return "answers";
		}
		String email = SecurityContextHolder.getContext().getAuthentication().getName().toString();
		User u = userRepo.findUserByEmail(email);
		u.setAnswered(true);
		u.setLanguages(answerForm.getLanguages());
		u.setModus(answerForm.getModus());
		u.setMotivation(answerForm.getMotivation());
		ArrayList<Boolean> questions = new ArrayList<Boolean>();
		questions.add(answerForm.getQ1());
		questions.add(answerForm.getQ2());
		questions.add(answerForm.getQ3());
		questions.add(answerForm.getQ4());
		questions.add(answerForm.getQ5());
		try {
			u.setQuestions(questions);
		} catch (Exception e) {
			// If not exactly 5 answers are given; this should never happen.
			e.printStackTrace();
		}
		userRepo.updateUser(u);
		return "profile";
	}
	
	private void addListsTo(Model model) {
		List<String> lang = new ArrayList<String>();
		lang.add("DE");
		lang.add("FR");
		lang.add("IT");
		lang.add("EN");
		model.addAttribute("lang", lang);
		
		List<String> modus = new ArrayList<String>();
		modus.add("Single");
		modus.add("Pair");
		modus.add("Either");
		model.addAttribute("modi", modus);
	}
}
