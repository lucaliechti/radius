package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import radius.web.components.ModelDecorator;

import java.util.Locale;

@Controller
@RequestMapping(value="/logout")
public class LogoutController {
	
	private ModelDecorator modelDecorator;

	public LogoutController(ModelDecorator modelDecorator) {
		this.modelDecorator = modelDecorator;
	}
	
	@RequestMapping(method=GET)
	public String logoutPage (Model model, Locale loc) {
		model.addAttribute("loggedout", Boolean.TRUE);
		model.addAllAttributes(modelDecorator.homeAttributes(loc));
		return "home";
	}
}
