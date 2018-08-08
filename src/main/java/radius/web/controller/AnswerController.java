package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.ArrayList;
import java.util.Arrays;
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
		System.out.println("in the AnswerController class after GET request");
		String email = SecurityContextHolder.getContext().getAuthentication().getName().toString();
		if(!userRepo.userHasAnswered(email)) {
			model.addAttribute("newUser", true);
			model.addAttribute("answerForm", new AnswerForm());
		}
		else {
			AnswerForm f = newFormFromUser(userRepo.findUserByEmail(email));
			model.addAttribute("answerForm", f);
		}
		addListsTo(model);
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
		u = updateUserFromForm(u, answerForm);
		u.setAnswered(true);
		userRepo.updateUser(u);
		return "status";
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
	
	private AnswerForm newFormFromUser(User u) {
		AnswerForm f = new AnswerForm();
		f.setMotivation(u.getMotivation());
		f.setModus(u.getModusAsString()); // TODO: this has to be re-done with tri-lingual modi
		f.setLanguages(u.getLanguages());
		/* TESTING
		String loc = "";
		for(Integer locid : u.getLocations()) {
			if(loc==""){
				loc += locid;
			}
			else {
				loc += ";"+locid;
			}
		}
		f.setLocations(loc);
		*/
		f.setLocations(User.createLocString(u.getLocations()));
		f.setQ1(u.getQuestions().get(0));
		f.setQ2(u.getQuestions().get(1));
		f.setQ3(u.getQuestions().get(2));
		f.setQ4(u.getQuestions().get(3));
		f.setQ5(u.getQuestions().get(4));
		
		return f;
	}
	
	private User updateUserFromForm(User u, AnswerForm answerForm) {
		u.setLanguages(answerForm.getLanguages());
		u.setModus(answerForm.getModus());
		u.setMotivation(answerForm.getMotivation().length() == 0 ? null : answerForm.getMotivation());
		ArrayList<Boolean> questions = new ArrayList<Boolean>();
		questions.add(answerForm.getQ1());
		questions.add(answerForm.getQ2());
		questions.add(answerForm.getQ3());
		questions.add(answerForm.getQ4());
		questions.add(answerForm.getQ5());
		try {
			String[] loc = answerForm.getLocations().split(";");
			Integer[] locint = new Integer[loc.length];
			for(int i = 0; i < loc.length; i++) {
				locint[i] = Integer.parseInt(loc[i]);
			}
			List<Integer> locations = Arrays.asList(locint);
			u.setLocations(locations);
		}
		catch (NumberFormatException nfe) { System.out.print("Answercontroller: "); nfe.printStackTrace(); }
		
		try {
			u.setQuestions(questions);
		} catch (Exception e) {
			// If not exactly 5 answers are given; this should never happen.
			e.printStackTrace();
		}
		return u;
	}


}
