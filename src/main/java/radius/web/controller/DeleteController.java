package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import radius.data.repository.JDBCUserRepository;
import radius.data.repository.UserRepository;
import radius.exceptions.UserHasMatchesException;
import radius.web.components.ModelRepository;

@Controller
@RequestMapping(value="/delete")
public class DeleteController {

	private ModelRepository modelRepository;
	private UserRepository userRepo;
	private ProfileController pc;

	public DeleteController(ModelRepository modelRepository, JDBCUserRepository userRepo, ProfileController pc) {
		this.modelRepository = modelRepository;
		this.userRepo = userRepo;
		this.pc = pc;
	}

	@RequestMapping(method=GET)
	public String reset(Model model) {
		model.addAllAttributes(modelRepository.homeAttributes());
		return "home";
	}

	@RequestMapping(method=POST)
	public String contact(Model model) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		try {
			userRepo.deleteUser(username);
		} catch(UserHasMatchesException e) {
			model.addAttribute("delete_failed", true);
			return pc.profile(null, model);
		}
		model.addAttribute("delete_success", username);
		SecurityContextHolder.clearContext();
		model.addAllAttributes(modelRepository.homeAttributes());
		return "home";
	}
}
