package reach.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import reach.data.ExperienceRepository;

@Controller
@RequestMapping(value="/experience")
public class ExperienceController {
	
	private ExperienceRepository repo;
		
	@Autowired
	public ExperienceController(ExperienceRepository _repo) {
		this.repo = _repo;
	}
	
	@RequestMapping(method=GET)
	public String experience(Model model) {
		System.out.println("preparing the experience model");
		model.addAttribute("experiences", repo.allExperiences());
		model.addAttribute("yolo", "blah");
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