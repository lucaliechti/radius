package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import radius.UserForm;
import radius.data.JDBCStaticResourceRepository;


@Controller
@RequestMapping(value="/login")
public class LoginController {

	private JDBCStaticResourceRepository staticRepo;

	@Autowired
	public LoginController(JDBCStaticResourceRepository _staticRepo) {
		this.staticRepo = _staticRepo;
	}

	@RequestMapping(method=GET)
	public String login(@RequestParam(value = "error", required = false) String loginerror, Model model) {
		System.out.println("in the LoginController class");
		if(loginerror != null) {
			model.addAttribute("loginerror", new Boolean(true));
		}
		model.addAttribute("registrationForm", new UserForm());
		model.addAttribute("cantons", staticRepo.cantons());
		return "home";
	}
}