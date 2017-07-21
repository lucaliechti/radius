package reach.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/contact")
public class ContactController {

	@RequestMapping(method=GET)
	public String contact() {
		System.out.println("in the ContactController class");
		return "contact";
	}
}
