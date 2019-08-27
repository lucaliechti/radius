package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import radius.data.JDBCStaticResourceRepository;
import radius.data.JDBCUUIDRepository;
import radius.data.JDBCUserRepository;
import radius.data.StaticResourceRepository;
import radius.data.UUIDRepository;
import radius.data.UserRepository;

@Controller
@RequestMapping(value="/confirm")
public class ConfirmEmailController {
	
	private UserRepository userRepo;
	private UUIDRepository uuidRepo;
	private StaticResourceRepository staticRepo;

	@Autowired
	private HomeController h;
	
	public ConfirmEmailController(JDBCUserRepository _userRepo, JDBCUUIDRepository _uuidRepo, JDBCStaticResourceRepository _staticRepo) {
		this.userRepo = _userRepo;
		this.uuidRepo = _uuidRepo;
		this.staticRepo = _staticRepo;
	}
	
	@RequestMapping(method=GET)
	public String confirm(@RequestParam(value = "uuid", required = true) String uuid, Model model) {
		String userEmail;
		try {
			userEmail = uuidRepo.findUserByUUID(uuid);
		}
		catch (IncorrectResultSizeDataAccessException irsdae) {
			model.addAttribute("confirmation_error", true);
			return h.cleanlyHome(model);
		}
		if(userEmail != null) {
			userRepo.enableUser(userEmail);
			uuidRepo.removeUser(userEmail);
			model.addAttribute("enable_success", true); ///////////??????????????
		}
		model.addAttribute("emailconfirmed", true);
		return h.cleanlyHome(model);
	}
}
