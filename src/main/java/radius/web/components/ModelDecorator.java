package radius.web.components;

import org.springframework.stereotype.Component;
import radius.data.dto.EmailDto;
import radius.data.form.UserForm;

import java.util.HashMap;
import java.util.Map;

@Component
public class ModelDecorator {

    private CountrySpecificProperties countryProperties;
    private RealWorldProperties realWorld;

    public ModelDecorator(CountrySpecificProperties countryProperties, RealWorldProperties realWorld) {
        this.countryProperties = countryProperties;
        this.realWorld = realWorld;
    }

    public Map<String, Object> homeAttributes() {
        HashMap<String, Object> homeAttributes = new HashMap<>();
        homeAttributes.put("registrationForm", new UserForm());
        homeAttributes.put("cantons", countryProperties.getCantons());
        homeAttributes.put("newsletterForm", new EmailDto());
        return homeAttributes;
    }

    public Map<String, Object> answerAttributes() {
        HashMap<String, Object> answerAttributes = new HashMap<>();
        answerAttributes.put("lang", countryProperties.getLanguages());
        answerAttributes.put("nrQ", realWorld.getNumberOfRegularQuestions());
        answerAttributes.put("special", realWorld.isSpecialIsActive());
        answerAttributes.put("nrV", realWorld.getNumberOfVotes());
        answerAttributes.put("currentVote", realWorld.getCurrentVote());
        return answerAttributes;
    }

}
