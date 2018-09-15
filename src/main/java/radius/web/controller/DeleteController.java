package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import radius.UserForm;
import radius.data.JDBCStaticResourceRepository;
import radius.data.JDBCUserRepository;
import radius.data.StaticResourceRepository;
import radius.data.UserRepository;

@Controller
@RequestMapping(value="/delete")
public class DeleteController {
	
	private UserRepository userRepo;
	private StaticResourceRepository staticRepo;
	
	public DeleteController(JDBCUserRepository _userRepo, JDBCStaticResourceRepository _staticRepo) {
		this.userRepo = _userRepo;
		this.staticRepo = _staticRepo;
	}

	@RequestMapping(method=POST)
	public String contact(Model model) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		SecurityContextHolder.clearContext();
		userRepo.deleteUser(username);
		model.addAttribute("delete_success", username);
		model.addAttribute("registrationForm", new UserForm());
		model.addAttribute("cantons", staticRepo.cantons());
		return "home";
	}
}
