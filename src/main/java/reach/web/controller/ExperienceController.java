package reach.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import reach.data.DefaultExperienceRepository;
import reach.data.ExperienceRepository;
import reach.data.JDBCExperienceRepository;
//import reach.data.HibernateExperienceRepository;

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
		System.out.println("preparing the experience model");
		model.addAttribute("experiences", repo.allExperiences());
		return "experience";
	}
	
	/*
	@RequestMapping(method=GET)
	public ModelAndView experience(Model model) {
		ModelAndView exp = new ModelAndView("experience");
		exp.addObject("experiences", repo.allExperiences());
		return exp;
	}
	*/
}