package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import radius.data.JDBCUserRepository;
import radius.data.UserRepository;

@Controller
@RequestMapping(value="/toggleStatus")
public class ToggleStatusController {
	
	private UserRepository userRepo;
	private ProfileController pc;
	
	public ToggleStatusController(JDBCUserRepository _userRepo, ProfileController _pc) {
		this.userRepo = _userRepo;
		this.pc = _pc;
	}
	
	@RequestMapping(method=GET)
	public String toggle(Model model) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName().toString();
		if(userRepo.userIsActive(email)) {
			userRepo.deactivateUser(email);
		}
		else {
			userRepo.activateUser(email);
		}
		return pc.profile(null, model);
	}
}
