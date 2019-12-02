package radius.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import radius.data.form.SurveyForm;
import radius.data.repository.*;

import javax.validation.Valid;
import java.util.Locale;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class SurveyController {

    private StaticResourceRepository staticRepo;
    private RegistrationController registrationController;
    private HomeController homeController;
    private SurveyRepository surveyRepo;
    private NewsletterRepository newsletterRepo;
    private final int SURVEY_SIZE = 15;

    public SurveyController(JDBCStaticResourceRepository staticRepo, RegistrationController r, HomeController h,
                            JDBCSurveyRepository surveyRepo, JDBCNewsletterRepository newsletterRepo) {
        this.staticRepo = staticRepo;
        this.registrationController = r;
        this.homeController = h;
        this.surveyRepo = surveyRepo;
        this.newsletterRepo = newsletterRepo;
    }

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

        try {
            surveyRepo.saveAnswers(surveyForm.getQuestions(), surveyForm.getAnswers(), wantsNewsletter, wantsToRegister);
        } catch(Exception e) {
            e.printStackTrace();
            model.addAttribute("surveyFailure", Boolean.TRUE);
            model.addAttribute("surveyForm", surveyForm);
            model.addAttribute("cantons", staticRepo.cantons());
            model.addAttribute("nrQ", SURVEY_SIZE);
            return "survey";
        }
        model.addAttribute("surveySuccess", Boolean.TRUE);

        if(wantsToRegister) {
            String firstName = surveyForm.getFirstName();
            String lastName = surveyForm.getLastName();
            String canton = surveyForm.getCanton();
            String emailR = surveyForm.getEmailR();
            String password = surveyForm.getPassword();
            return registrationController.cleanlyRegisterNewUser(model, loc, firstName, lastName, canton, emailR, password);
        }

        else if(wantsNewsletter) {
            String emailN = surveyForm.getEmailN();
            try {
                newsletterRepo.subscribe(emailN, "Survey Summer 2019");
            } catch (Exception e) {
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
