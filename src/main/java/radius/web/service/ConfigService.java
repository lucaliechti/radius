package radius.web.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import radius.data.form.ConfigurationForm;
import radius.data.form.QuestionForm;
import radius.data.repository.ConfigRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
@Getter
@Component
public class ConfigService {

    private ConfigurationForm configurationForm;
    private QuestionForm questionForm;
    private ConfigRepository configRepository;
    private final int LANGUAGE_ORDER_GERMAN = 0;
    private final int LANGUAGE_ORDER_FRENCH = 1;
    private final int LANGUAGE_ORDER_ENGLISH = 2;

    public ConfigService(ConfigRepository configRepository) {
        this.configRepository = configRepository;
        updateFromDatabase();
    }

    public void updateConfig(ConfigurationForm form) {
        log.info("Updating configuration from form");
        try {
            configRepository.updateConfiguration(form);
        } catch (SQLException sqle) {
            log.error("Error saving configuration to database.");
        }
        updateFromDatabase();
    }

    public void updateQuestions(QuestionForm form) {
        log.info("Updating questions from form");
        try {
            configRepository.updateQuestions(form);
        } catch (SQLException sqle) {
            log.error("Error saving questions to database.");
        }
        updateFromDatabase();
    }

    private void updateFromDatabase() {
        try {
            this.configurationForm = configRepository.getConfig();
            this.questionForm = configRepository.getQuestions();
        } catch (SQLException sqle) {
            log.error("Error retrieving configuration from database.");
        }
    }

    public ConfigurationForm getForm() {
        return configurationForm;
    }

    public QuestionForm getQuestionForm() {
        return questionForm;
    }

    public List<String> regularQuestions(Locale locale) {
        switch(locale.getLanguage()) {
            case "de":
                return this.questionForm.getRegularQuestions().get(LANGUAGE_ORDER_GERMAN);
            case "fr":
                return this.questionForm.getRegularQuestions().get(LANGUAGE_ORDER_FRENCH);
            case "en":
                return this.questionForm.getRegularQuestions().get(LANGUAGE_ORDER_ENGLISH);
            default:
                return new ArrayList<>();
        }
    }
    public List<String> specialQuestions(Locale locale) {
        switch(locale.getLanguage()) {
            case "de":
                return this.questionForm.getSpecialQuestions().get(LANGUAGE_ORDER_GERMAN);
            case "fr":
                return this.questionForm.getSpecialQuestions().get(LANGUAGE_ORDER_FRENCH);
            case "en":
                return this.questionForm.getSpecialQuestions().get(LANGUAGE_ORDER_ENGLISH);
            default:
                return new ArrayList<>();
        }
    }
    public boolean matchingFactorWaitingTime() {
        return this.configurationForm.isMatchingFactorWaitingTime();
    }
    public boolean matchingActive() {
        return this.configurationForm.isMatchingActive();
    }
    public int matchingMinimumDisagreementsRegular() {
        return this.configurationForm.getMatchingMinimumDisagreementsRegular();
    }
    public int matchingMinimumDisagreementsSpecial() {
        return this.configurationForm.getMatchingMinimumDisagreementsSpecial();
    }
    public boolean specialActive() {
        return this.configurationForm.isSpecialActive();
    }
    public int numberOfVotes() {
        return this.configurationForm.getNumberOfVotes();
    }
    public int numberOfRegularQuestions() {
        return this.configurationForm.getNumberOfRegularQuestions();
    }
    public String currentVote() {
        return this.configurationForm.getCurrentVote();
    }

}
