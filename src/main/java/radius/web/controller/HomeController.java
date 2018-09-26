package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import javax.annotation.Resource;

//import java.util.Locale;
//import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.servlet.LocaleResolver;

//import radius.ReminderForm;
import radius.UserForm;
//import radius.data.JDBCReminderRepository;
import radius.data.JDBCStaticResourceRepository;
import radius.web.components.EmailService;

@Controller
@RequestMapping(value={"/", "/home"})
@ComponentScan("radius.config") 
public class HomeController {

	private JDBCStaticResourceRepository staticRepo;
	
	@Qualifier("helloMailSender")
	@Autowired
	private JavaMailSenderImpl helloMailSender;
	
	@Qualifier("matchingMailSender")
	@Autowired
	private JavaMailSenderImpl matchingMailSender;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	public HomeController(JDBCStaticResourceRepository _staticRepo) {
		this.staticRepo = _staticRepo;
	}
	
	@RequestMapping(method=GET)
	public String home(@RequestParam(value = "logout", required = false) String loggedout, @RequestParam(value = "error", required = false) String error, Model model, HttpServletRequest req, HttpServletResponse res) {

		System.out.println("In the HomeController class");
		
		model.addAttribute("registrationForm", new UserForm());
		model.addAttribute("cantons", staticRepo.cantons());
		
		if(loggedout != null) {
			model.addAttribute("loggedout", "user sees this page right after logging out");
		}
		
		if(error != null) {
			model.addAttribute("loginerror", "user sees this page right after logging out");
		}

		if(model.containsAttribute("success")) {
			model.addAttribute("success", -1);
		}
		emailService.sendSimpleMessage("luca.liechti@gmail.com", "hi from hello", "this is a message from hello@radius-schweiz.ch", helloMailSender);
		emailService.sendSimpleMessage("luca.liechti@gmail.com", "hi from matching", "this is a message from matching@radius-schweiz.ch", matchingMailSender);
		
		return "home";
	}
	/*
	 * LL: This was only for the reminder registration.
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
	*/
}
