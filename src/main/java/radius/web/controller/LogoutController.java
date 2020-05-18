package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import radius.web.components.ModelDecorator;

import java.util.Locale;

@Controller
@RequiredArgsConstructor
@RequestMapping(value="/logout")
public class LogoutController {
	
	private final ModelDecorator modelDecorator;

	@RequestMapping(method=GET)
	public String logoutPage (Model model, Locale loc) {
		model.addAttribute("loggedout", Boolean.TRUE);
		model.addAllAttributes(modelDecorator.homeAttributes(loc));
		return "home";
	}
}
