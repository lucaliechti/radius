package radius.web.components;

import org.springframework.stereotype.Component;
import radius.data.dto.EmailDto;
import radius.data.form.UserForm;
import radius.web.service.ConfigService;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
public class ModelDecorator {

    private CountrySpecificProperties countryProperties;
    private ConfigService configService;

    public ModelDecorator(CountrySpecificProperties countryProperties, ConfigService configService) {
        this.countryProperties = countryProperties;
        this.configService = configService;
    }

    public Map<String, Object> homeAttributes() {
        HashMap<String, Object> homeAttributes = new HashMap<>();
        homeAttributes.put("registrationForm", new UserForm());
        homeAttributes.put("cantons", countryProperties.getCantons());
        homeAttributes.put("newsletterForm", new EmailDto());
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

}
