package radius.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import java.util.Locale;
import java.util.List;
import java.util.ArrayList;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper; //TEST

@Controller
@RequestMapping(value={"/", "/home"})
public class HomeController {

	@RequestMapping(method=GET)
	public String home(@RequestParam(value = "logout", required = false) String loggedout, Locale loc, Model model) {
//		System.out.println("in the HomeController class");
//		System.out.println("Locale = " + loc);
		if(loggedout != null) {
			model.addAttribute("loggedout", "user sees this page right after logging out");
		}
		
		/////////////////////////
		////////////TEST/////////
		/////////////////////////
		ObjectMapper mapper = new ObjectMapper();
		
		List<String> locs = new ArrayList<String>();
		locs.add("Bern");
		locs.add("Herisau");
		String jsonResult = "";
		try {
			jsonResult = mapper.writeValueAsString(locs);
		}
		catch (Exception e) {e.printStackTrace();}
		model.addAttribute("locs", jsonResult);
		
		return "home";
	}
}
