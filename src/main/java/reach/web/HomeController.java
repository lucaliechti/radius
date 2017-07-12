package reach.web;

import static org.springframework.web.bind.annotation.RequestMethod.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value={"/", "/home"})
public class HomeController {

	@RequestMapping(method=GET)
	public String home() {
		System.out.println("in the HomeController class");
		return "home";
	}
}
