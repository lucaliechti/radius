package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import radius.data.JDBCUserRepository;
import radius.data.UserRepository;

@Controller
@RequestMapping(value="/confirm")
public class ConfirmEmailController {
	
	private UserRepository userRepo;
	private HomeController h;
	
	public ConfirmEmailController(JDBCUserRepository userRepo) {
		this.userRepo = userRepo;
	}
	
	@RequestMapping(method=GET)
	public String confirm(@RequestParam(value = "uuid") String uuid, Model model) {
		String userEmail;
		try {
			userEmail = userRepo.findEmailByUuid(uuid);
		}
		catch (IncorrectResultSizeDataAccessException irsdae) {
			model.addAttribute("confirmation_error", true);
			return h.cleanlyHome(model);
		}
		if(userEmail != null) {
			userRepo.enableUser(userEmail);
		}
		model.addAttribute("emailconfirmed", true);
		return h.cleanlyHome(model);
	}
}
