package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Locale;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import radius.data.JDBCUserRepository;
import radius.data.UserRepository;
import radius.exceptions.UserHasMatchesException;

@Controller
@RequestMapping(value="/delete")
public class DeleteController {

	private HomeController hc;
	private UserRepository userRepo;
	private ProfileController pc;

	public DeleteController(HomeController hc, JDBCUserRepository userRepo, ProfileController pc) {
		this.hc = hc;
		this.userRepo = userRepo;
		this.pc = pc;
	}

	@RequestMapping(method=GET)
	public String reset(Model model) {
		return hc.cleanlyHome(model);
	}

	@RequestMapping(method=POST)
	public String contact(Model model) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		try {
			userRepo.deleteUser(username);
		}
		catch(UserHasMatchesException e) {
			model.addAttribute("delete_failed", true);
			return pc.profile(null, model);
		}
		model.addAttribute("delete_success", username);
		SecurityContextHolder.clearContext();
		return hc.home(null, null, model);
	}
}
