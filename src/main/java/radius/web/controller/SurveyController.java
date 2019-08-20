package radius.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import radius.SurveyForm;
import radius.UserForm;
import radius.data.JDBCStaticResourceRepository;

import javax.validation.Valid;
import java.util.Locale;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class SurveyController {

    @Autowired
    private JDBCStaticResourceRepository staticRepo;

    @RequestMapping(value="/survey", method=GET)
    public String survey(Model model, Locale loc){
        model.addAttribute("surveyForm", new SurveyForm());
        model.addAttribute("cantons", staticRepo.cantons());
        return "survey";
    }

    @RequestMapping(value="/survey", method=POST)
    public String filledOutSurvey(@ModelAttribute("surveyForm") @Valid SurveyForm surveyForm, BindingResult result, Model model, Locale loc) {
        if(result.hasErrors()) {
            System.out.println("invalid surveyForm");
            model.addAttribute("surveyForm", surveyForm);
            model.addAttribute("cantons", staticRepo.cantons());
            return "survey";
        }

        //save questions

        //If Newsletter: save email address

        //If registered: save User, send email etc.
        //TODO
        model.addAttribute("waitForEmailConfirmation", Boolean.TRUE);




        model.addAttribute("registrationForm", new UserForm());
        model.addAttribute("cantons", staticRepo.cantons());
        return "home";

    }
}
