package radius.web.controller;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import radius.data.dto.PressreleaseDto;
import radius.data.form.*;
import radius.web.service.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class AdminController {

    private NewsletterService newsletterservice;
    private UserService userService;
    private SurveyService surveyService;
    private MatchingService matchingService;
    private ConfigService configService;
    private PressService pressService;

    private SimpleDateFormat pressReleasePrefixFormat = new SimpleDateFormat("yyyyMMdd");
    private final String RADIUS = "Radius";
    private final String STATIC_DIRECTORY = "static";
    private final int POSITION_GERMAN = 0;
    private final String RELEASE_GERMAN = "Medienmitteilung";
    private final String RELEASE_FRENCH = "Communique";

    public AdminController(NewsletterService newsletterservice, UserService userService, SurveyService surveyService,
                           MatchingService matchingService, ConfigService configService, PressService pressService) {
        this.newsletterservice = newsletterservice;
        this.userService = userService;
        this.surveyService = surveyService;
        this.matchingService = matchingService;
        this.configService = configService;
        this.pressService = pressService;
    }

    @RequestMapping(path="/admin")
    public String admin() {
        return "admin";
    }

    @RequestMapping(path="/edit/ban", method=GET)
    public String banUser(@RequestParam(value = "uuid") String uuid, Model model) {
        Optional<String> optionalUser = userService.findEmailByUuid(uuid);
        if(optionalUser.isPresent()) {
            if(userService.banUser(optionalUser.get())) {
                model.addAttribute("success", Boolean.TRUE);
            } else {
                model.addAttribute("failure", Boolean.TRUE);
            }
        }
        model.addAttribute("users", userService.allUsers());
        model.addAttribute("activetab", "users");
        return "admin";
    }

    @RequestMapping(path="/edit/delete", method=GET)
    public String deleteUser(@RequestParam(value = "uuid") String uuid, Model model) {
        Optional<String> optionalUser = userService.findEmailByUuid(uuid);
        if(optionalUser.isPresent()) {
            if(userService.deleteUser(optionalUser.get())) {
                model.addAttribute("success", Boolean.TRUE);
            } else {
                model.addAttribute("failure", Boolean.TRUE);
            }
        }
        model.addAttribute("users", userService.allUsers());
        model.addAttribute("matches", matchingService.uniqueOrderedMatches());
        model.addAttribute("activetab", "users");
        return "admin";
    }

    @RequestMapping(path="/edit/private", method=GET)
    public String setPrivate(@RequestParam(value = "uuid") String uuid, Model model) {
        Optional<String> optionalUser = userService.findEmailByUuid(uuid);
        if(optionalUser.isPresent()) {
            if(userService.setUserPrivate(optionalUser.get())) {
                model.addAttribute("success", Boolean.TRUE);
            } else {
                model.addAttribute("failure", Boolean.TRUE);
            }
        }
        model.addAttribute("users", userService.allUsers());
        model.addAttribute("activetab", "users");
        return "admin";
    }

    @RequestMapping(path="/edit/unsubscribe", method=GET)
    public String unsubscribeNewsletter(@RequestParam(value = "uuid") String uuid, Model model) {
        if(newsletterservice.unsubscribe(uuid)){
            model.addAttribute("success", Boolean.TRUE);
        } else {
            model.addAttribute("failure", Boolean.TRUE);
        }
        model.addAttribute("newsletterRecipients", newsletterservice.allRecipients());
        model.addAttribute("activetab", "newsletter");
        return "admin";
    }

    @RequestMapping(path="/update/configuration", method=POST)
    public String updateConfiguration(@ModelAttribute("configurationForm") @Valid ConfigurationForm form,
                                      BindingResult result, Model model) {
        model.addAttribute("activetab", "config");
        if(result.hasErrors()) {
            return "admin";
        }
        configService.updateConfig(form);
        model.addAttribute("configupdate_success", Boolean.TRUE);
        model.addAttribute("configurationForm", configService.getForm());
        return "admin";
    }

    @RequestMapping(path="/update/questions", method=POST)
    public String updateQuestions(@ModelAttribute("questionForm") @Valid QuestionForm form, BindingResult result,
                                  Model model) {
        model.addAttribute("activetab", "questions");
        if(result.hasErrors()) {
            return "admin";
        }
        configService.updateQuestions(form);
        model.addAttribute("configupdate_success", Boolean.TRUE);
        model.addAttribute("configurationForm", configService.getForm());
        return "admin";
    }

    @RequestMapping(path="/mail/users", method=POST)
    public String contactUsers(@ModelAttribute("contactUserForm") @Valid NewsletterForm contactUserForm,
                               BindingResult result, Model model) {
        model.addAttribute("activetab", "users");
        if(result.hasErrors()) {
            return "admin";
        }
        List<String> recipients = Arrays.asList(contactUserForm.getRecipients().split(";"));
        List<String> failed = newsletterservice.sendMassEmail(false,
                contactUserForm.getSubject(),
                contactUserForm.getMessage(),
                recipients,
                new Locale(contactUserForm.getLanguage()));
        decorateModel(model, recipients, failed);
        model.addAttribute("contactUserForm", new NewsletterForm());
        return "admin";
    }

    @RequestMapping(path="/mail/newsletter", method=POST)
    public String sendNewsletter(@ModelAttribute("newsletterForm") @Valid NewsletterForm newsletterForm,
                                 BindingResult result, Model model) {
        model.addAttribute("activetab", "newsletter");
        if(result.hasErrors() || newsletterForm.getLanguage() == null) {
            return "admin";
        }
        List<String> recipients = Arrays.asList(newsletterForm.getRecipients().split(";"));
        List<String> failed = newsletterservice.sendMassEmail(true,
                newsletterForm.getSubject(),
                newsletterForm.getMessage(),
                recipients,
                new Locale(newsletterForm.getLanguage()));
        decorateModel(model, recipients, failed);
        model.addAttribute("newsletterForm", new NewsletterForm());
        return "admin";
    }

    private void decorateModel(Model model, List<String> recipients, List<String> failed) {
        int success = recipients.size() - failed.size();
        if(success > 0) {
            model.addAttribute("successfullySent", success);
        }
        if(failed.size() > 0) {
            model.addAttribute("failedRecipients", String.join("\n", failed));
        }
    }

    @RequestMapping(path="/update/**", method=GET)
    public String getUpdate() {
        return "admin";
    }

    @RequestMapping(path="/mail/**", method=GET)
    public String getMail() {
        return "admin";
    }

    @RequestMapping(path="/edit/**", method=GET)
    public String getEdit() {
        return "admin";
    }

    @RequestMapping(path="/press/**", method=GET)
    public String getPress() {
        return "admin";
    }

    @RequestMapping(path="/press/mention", method=POST)
    public String addMention(@ModelAttribute("mentionForm") @Valid MentionForm form, BindingResult result, Model model) {
        model.addAttribute("activetab", "press");
        if(result.hasErrors()) {
            return "admin";
        }
        try {
            pressService.addMention(form);
            model.addAttribute("success", Boolean.TRUE);
            model.addAttribute("mentionForm", new MentionForm());
        } catch (Exception e) {
            model.addAttribute("failure", Boolean.TRUE);
        }
        return "admin";
    }

    @RequestMapping(path="/press/release", method=POST)
    public String uploadPressRelease(@ModelAttribute("pressreleaseForm") @Valid PressreleaseForm form,
                                     BindingResult result, Model model, HttpServletRequest request) throws IOException {
        model.addAttribute("activetab", "press");
        if(form.getFile_de().isEmpty()) {
            FieldError error = new FieldError("form", "file_de",
                    "You must upload a file.");
            result.addError(error);
        } else if (form.getFile_fr().isEmpty()) {
            FieldError error = new FieldError("form", "file_fr",
                    "You must upload a file.");
            result.addError(error);
        }
        if(result.hasErrors()) {
            return "admin";
        }
        PressreleaseDto dto = new PressreleaseDto();
        dto.setDate(form.getDate());
        String[] newLinks = new String[2];
        String datePrefix = pressReleasePrefixFormat.format(form.getDate());
        String baseFilePath = request.getServletContext().getRealPath("/" + STATIC_DIRECTORY + "/");
        if(Files.notExists(Paths.get(baseFilePath))) {
            File basePath = new File(baseFilePath);
            basePath.mkdir();
        }
        for(int i = 0; i <= 1; i++) { //magic numbers
            String releaseString = i == POSITION_GERMAN ? RELEASE_GERMAN : RELEASE_FRENCH;
            MultipartFile file = i == POSITION_GERMAN ? form.getFile_de() : form.getFile_fr();
            String fileEnding = FilenameUtils.getExtension(file.getOriginalFilename());
            String newFileName = String.join(".", String.join("_", datePrefix, RADIUS, releaseString), fileEnding);
            String newFilePath = baseFilePath + newFileName;
            file.transferTo(new File(newFilePath));
            newLinks[i] = String.join("/", STATIC_DIRECTORY, newFileName);
        }
        dto.setLinks(newLinks);
        try {
            pressService.addPressrelease(dto);
            model.addAttribute("success", Boolean.TRUE);
            model.addAttribute("pressreleaseForm", new PressreleaseForm());
        } catch (Exception e) {
            model.addAttribute("failure", Boolean.TRUE);
        }
        return "admin";
    }

    @RequestMapping(path="press/publishnews", method=POST)
    public String publishNews(@ModelAttribute("newsForm") @Valid NewsForm form, BindingResult result, Model model) {
        model.addAttribute("activetab", "news");
        if(result.hasErrors()) {
            return "admin";
        }
        try {
            pressService.addNews(form);
            model.addAttribute("success", Boolean.TRUE);
        } catch (Exception e) {
            model.addAttribute("failure", Boolean.TRUE);
        }
        return "admin";
    }

    @RequestMapping(path="/runMatching")
    public String runMatching(Model model) {
        model.addAttribute("activetab", "matches");
        try {
            matchingService.matchUsers();
            model.addAttribute("success", Boolean.TRUE);
        } catch (Exception e) {
            model.addAttribute("failure", Boolean.TRUE);
        }
        model.addAttribute("matches", matchingService.uniqueOrderedMatches());
        return "admin";
    }

    @ModelAttribute
    public void prepare(Model model) {
        model.addAttribute("statistics", userService.getStatistics());
        model.addAttribute("newsForm", new NewsForm());
        model.addAttribute("pressreleaseForm", new PressreleaseForm());
        model.addAttribute("mentionForm", new MentionForm());
        model.addAttribute("configurationForm", configService.getForm());
        model.addAttribute("questionForm", configService.getQuestionForm());
        model.addAttribute("matches", matchingService.uniqueOrderedMatches());
        model.addAttribute("regionDensity", userService.regionDensity());
        model.addAttribute("surveyStats", surveyService.statistics());
        model.addAttribute("newsletterRecipients", newsletterservice.allRecipients());
        model.addAttribute("users", userService.allUsers());
        model.addAttribute("special", configService.specialActive());
        model.addAttribute("nrvotes", configService.numberOfVotes());
        model.addAttribute("newsletterForm", new NewsletterForm());
        model.addAttribute("contactUserForm", new NewsletterForm());
    }

}
