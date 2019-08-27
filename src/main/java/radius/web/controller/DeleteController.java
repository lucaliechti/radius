package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import radius.UserForm;
import radius.data.JDBCStaticResourceRepository;
import radius.data.UserRepository;
import radius.exceptions.UserHasMatchesException;

@Controller
@RequestMapping(value="/delete")
public class DeleteController {
	
	@Autowired
	private HomeController hc;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ProfileController pc;

	@Autowired
	private JDBCStaticResourceRepository staticRepo;

	@RequestMapping(method=GET)
	public String reset(Model model) {
		return hc.cleanlyHome(model);
	}

	@RequestMapping(method=POST)
	public String contact(Model model, HttpServletRequest req, HttpServletResponse res, Locale locale) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		try {
			userRepo.deleteUser(username);
		}
		catch(UserHasMatchesException e) {
			model.addAttribute("delete_failed", true);
			return pc.profile(null, model, locale);
		}
		model.addAttribute("delete_success", username);
		SecurityContextHolder.clearContext();
		return hc.home(null, null, model, req, res, locale);
	}
}
