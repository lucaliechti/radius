package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import radius.ReminderForm;
import radius.UserForm;
import radius.data.JDBCReminderRepository;
import radius.data.JDBCStaticResourceRepository;

@Controller
@RequestMapping(value={"/", "/home"})
@ComponentScan("radius.config")
public class HomeController {

	private JDBCStaticResourceRepository staticRepo;
	private JDBCReminderRepository reminderRepo;
//	private JDBCStaticResourceRepository staticResourceRepo;
//	private LocaleResolver lr;
	
	@Autowired
	public HomeController(JDBCStaticResourceRepository _staticRepo, JDBCReminderRepository _reminderRepo/*, LocaleResolver _lr*/) {
		this.staticRepo = _staticRepo;
		this.reminderRepo = _reminderRepo;
//		this.lr = _lr;
	}
	
	@RequestMapping(method=GET)
	public String home(@RequestParam(value = "logout", required = false) String loggedout, Model model, HttpServletRequest req, HttpServletResponse res) {
		/*
		String url = req.getRequestURL().toString();
		//only setting locale for the index page at the moment
		if(url.contains("radius-schweiz")) { lr.setLocale(req, res, new Locale("de")); }
		else if(url.contains("radius-suisse")) { lr.setLocale(req, res, new Locale("fr")); }
		else if(url.contains("radius-svizzera")) { lr.setLocale(req, res, new Locale("it")); }
		*/
		
		model.addAttribute("registrationForm", new UserForm());
		model.addAttribute("cantons", staticRepo.cantons());
		
		if(loggedout != null) {
			model.addAttribute("reminderForm", new ReminderForm());
			model.addAttribute("loggedout", "user sees this page right after logging out");
		}
		else {
			model.addAttribute("reminderForm", new ReminderForm());
		}
		if(model.containsAttribute("success")) {
			model.addAttribute("success", -1);
		}
		return "home";
	}
	
	@RequestMapping(method=POST)
	public String home(@ModelAttribute("reminderForm") @Valid ReminderForm reminderForm, BindingResult result, Model model) {
		if(result.hasErrors()){
			model.addAttribute("success", 0);
			return "index";
		}
		try {
			reminderRepo.saveReminder(reminderForm.getEmail());
			model.addAttribute("success", 1);
		}
		catch(Error e) {
			e.printStackTrace();
		}
		model.addAttribute("reminderForm", new ReminderForm());

		return "index";
	}
}
