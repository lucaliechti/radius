package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.*;

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

import radius.Experience;
import radius.data.ExperienceRepository;
import radius.data.JDBCExperienceRepository;

@Controller
@RequestMapping(value="/experience")
public class ExperienceController {
	
	private ExperienceRepository repo;
	
	//specify here which implementation of ExperienceRepository will be used
	@Autowired
	public ExperienceController(JDBCExperienceRepository _repo) {
		this.repo = _repo;
	}
	
	@RequestMapping(method=GET)
	public String experience(Model model) {
		List<Experience> experiences = new ArrayList<Experience>();
		experiences = repo.allExperiences();
		model.addAttribute("experiences", experiences);
		return "experience";
	}
	
	@RequestMapping(method=POST)
	public String addExperience(@ModelAttribute("experienceForm") @Valid Experience experienceForm, BindingResult result, Model model) {
		if(result.hasErrors()) {
			System.out.println("ExperienceController: Error with submitted Experience");
			return "writeexperience";
		}
		String exp = experienceForm.getExperience();
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		Experience e = new Experience(exp, name);
		repo.saveNewExperience(e);
		List<Experience> experiences = repo.allExperiences();
		model.addAttribute("experiences", experiences);
		return "experience";
	}
}