package reach.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/howto")
public class HowtoController {

	@RequestMapping(method=GET)
	public String reach() {
		System.out.println("in the HowtoController class");
		return "howto";
	}
}