package radius.web.components;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import radius.data.dto.EmailDto;
import radius.data.form.*;
import radius.web.service.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class ModelDecorator {

    private final CountrySpecificProperties countryProperties;
    private final ConfigService configService;
    private final PressService pressService;
    private final MatchingService matchingService;
    private final SurveyService surveyService;
    private final UserService userService;
    private final NewsletterService newsletterService;

    public Map<String, Object> homeAttributes(Locale locale) {
        HashMap<String, Object> homeAttributes = new HashMap<>();
        homeAttributes.put("registrationForm", new UserForm());
        homeAttributes.put("cantons", countryProperties.getCantons());
        homeAttributes.put("newsletterForm", new EmailDto());
        homeAttributes.put("newsreel", pressService.allNews(locale));
        return homeAttributes;
    }

    public Map<String, Object> answerAttributes(Locale locale) {
        HashMap<String, Object> answerAttributes = new HashMap<>();
        answerAttributes.put("lang", countryProperties.getLanguages());
        answerAttributes.put("nrQ", configService.numberOfRegularQuestions());
        answerAttributes.put("special", configService.specialActive());
        answerAttributes.put("nrV", configService.numberOfVotes());
        answerAttributes.put("currentVote", configService.currentVote());
        answerAttributes.put("regularQuestions", configService.regularQuestions(locale));
        if(configService.specialActive()) {
            answerAttributes.put("specialQuestions", configService.specialQuestions(locale));
        }
        return answerAttributes;
    }

    public Map<String, Object> adminAttributes() {
        HashMap<String, Object> model = new HashMap<>();
        model.put("statistics", userService.getStatistics());
        model.put("newsForm", new NewsForm());
        model.put("pressreleaseForm", new PressreleaseForm());
        model.put("mentionForm", new MentionForm());
        model.put("configurationForm", configService.getForm());
        model.put("questionForm", configService.getQuestionForm());
        model.put("matches", matchingService.uniqueOrderedMatches());
        model.put("regionDensity", userService.regionDensity());
        model.put("surveyStats", surveyService.statistics());
        model.put("newsletterRecipients", newsletterService.allRecipients());
        model.put("users", userService.allUsers());
        model.put("special", configService.specialActive());
        model.put("nrvotes", configService.numberOfVotes());
        model.put("newsletterForm", new NewsletterForm());
        model.put("contactUserForm", new NewsletterForm());
        return model;
    }

}
