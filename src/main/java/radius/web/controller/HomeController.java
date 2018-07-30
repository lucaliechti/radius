package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import radius.ReminderForm;
import radius.data.JDBCCantonRepository;
import radius.data.JDBCReminderRepository;
import radius.data.JDBCUserRepository;

//import com.fasterxml.jackson.databind.ObjectMapper; //TEST

@Controller
@RequestMapping(value={"/", "/home"})
public class HomeController {

	private JDBCReminderRepository reminderRepo;
	
	@Autowired
	public HomeController(JDBCReminderRepository _reminderRepo) {
		this.reminderRepo = _reminderRepo;
	}
	
	@RequestMapping(method=GET)
	public String home(@RequestParam(value = "logout", required = false) String loggedout, Locale loc, Model model) {
//		System.out.println("in the HomeController class");
//		System.out.println("Locale = " + loc);
		if(loggedout != null) {
			model.addAttribute("loggedout", "user sees this page right after logging out");
		}
		else {
			model.addAttribute("reminderForm", new ReminderForm());
		}
		if(model.containsAttribute("success")) {
			model.addAttribute("success", -1);
		}
		return "index";
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
