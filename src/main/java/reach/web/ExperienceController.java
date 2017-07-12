package reach.web;

import static org.springframework.web.bind.annotation.RequestMethod.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/experience")
public class ExperienceController {

	@RequestMapping(method=GET)
	public String experience() {
		System.out.println("in the ExperienceController class");
		return "experience";
	}
}