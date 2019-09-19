package radius.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import radius.SurveyForm;
import radius.data.JDBCNewsletterRepository;
import radius.data.JDBCStaticResourceRepository;
import radius.data.JDBCSurveyRepository;

import javax.validation.Valid;
import java.util.Locale;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class SurveyController {

    @Autowired
    private JDBCStaticResourceRepository staticRepo;

    @Autowired
    private RegistrationController registrationController;

    @Autowired
    private HomeController homeController;

    @Autowired
    private JDBCSurveyRepository surveyRepo;

    @Autowired
    private JDBCNewsletterRepository newsletterRepo;

    private final int SURVEY_SIZE = 15;

    @RequestMapping(value="/survey", method=GET)
    public String survey(Model model, Locale loc){
        model.addAttribute("surveyForm", new SurveyForm());
        model.addAttribute("cantons", staticRepo.cantons());
        model.addAttribute("nrQ", SURVEY_SIZE);
        return "survey";
    }

    @RequestMapping(value="/survey", method=POST)
    public String filledOutSurvey(@ModelAttribute("surveyForm") @Valid SurveyForm surveyForm, BindingResult result, Model model, Locale loc) {
        if(result.hasErrors()) {
            System.out.println("invalid surveyForm");
            model.addAttribute("surveyForm", surveyForm);
            model.addAttribute("cantons", staticRepo.cantons());
            model.addAttribute("nrQ", SURVEY_SIZE);
            return "survey";
        }

        boolean wantsNewsletter = surveyForm.getNewsletter();
        boolean wantsToRegister = surveyForm.getRegistration();

        //save questions
        try {
            surveyRepo.saveAnswers(surveyForm.getQuestions(), surveyForm.getAnswers(), wantsNewsletter, wantsToRegister);
        }
        catch(Exception e) {
            e.printStackTrace();
            model.addAttribute("surveyFailure", Boolean.TRUE);
            model.addAttribute("surveyForm", surveyForm);
            model.addAttribute("cantons", staticRepo.cantons());
            model.addAttribute("nrQ", SURVEY_SIZE);
            return "survey";
        }
        model.addAttribute("surveySuccess", Boolean.TRUE);

        //user wants to register - don't care about the newsletter
        if(wantsToRegister) {
            String firstName = surveyForm.getFirstName();
            String lastName = surveyForm.getLastName();
            String canton = surveyForm.getCanton();
            String emailR = surveyForm.getEmailR();
            String password = surveyForm.getPassword();

            return registrationController.cleanlyRegisterNewUser(model, loc, firstName, lastName, canton, emailR, password);
        }

        //only newsletter, no registration
        else if(wantsNewsletter) {
            String emailN = surveyForm.getEmailN();
            try {
                newsletterRepo.subscribe(emailN, "Survey Summer 2019");
            }
            catch(Exception e) {
                e.printStackTrace();
                model.addAttribute("surveyFailure", Boolean.TRUE);
                model.addAttribute("surveyForm", surveyForm);
                model.addAttribute("cantons", staticRepo.cantons());
                model.addAttribute("nrQ", SURVEY_SIZE);
                return "survey";
            }
        }

        return homeController.cleanlyHome(model);
    }
}
