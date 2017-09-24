package reach.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import reach.Experience;
//import reach.data.DefaultExperienceRepository;
import reach.data.ExperienceRepository;
import reach.data.JDBCExperienceRepository;
//import reach.data.HibernateExperienceRepository;

@Controller
@RequestMapping(value="/experience")
public class ExperienceController {
	
	//TODO: Think about id generation
	Long i = 11l;
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
//		String username = SecurityContextHolder.getContext().getAuthentication().getName();
//		model.addAttribute("username", username);
		return "experience";
	}
	
	@RequestMapping(method=POST)
	public String addExperience(@ModelAttribute("experienceForm") Experience experienceForm, Model model) {
		String exp = experienceForm.getExperience();
		String place = experienceForm.getPlace();
		String name = experienceForm.getName();
		Experience e = new Experience(i++, exp, place, name);
		repo.saveExperience(e);
		//Does this have to be here two times (GET / POST)?
		List<Experience> experiences = new ArrayList<Experience>();
		experiences = repo.allExperiences();
		model.addAttribute("experiences", experiences);
//		String username = SecurityContextHolder.getContext().getAuthentication().getName();
//		model.addAttribute("username", username);
		return "experience";
	}
	
}