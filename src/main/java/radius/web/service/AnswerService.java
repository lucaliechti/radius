package radius.web.service;

import org.springframework.stereotype.Service;
import radius.User;
import radius.User.TernaryAnswer;
import radius.data.form.AnswerForm;
import radius.web.components.ConfigurationProperties;
import radius.web.components.RealWorldProperties;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnswerService {

    private RealWorldProperties realWorld;
    private ConfigurationProperties config;

    public AnswerService(RealWorldProperties realWorld, ConfigurationProperties configurationProperties) {
        this.realWorld = realWorld;
        this.config = configurationProperties;
    }

    public void updateUserFromAnswerForm(User user, AnswerForm answerForm) {
        user.setStatus(User.UserStatus.WAITING);
        user.setLanguages(answerForm.getLanguages());
        user.setMotivation(answerForm.getMotivation());
        user.setRegularanswers(answerForm.getRegularanswers());
        user.setSpecialanswers(answerForm.getSpecialanswers());
        user.setLocations(Arrays.stream(answerForm.getLocations().split(";")).map(Integer::valueOf)
                .collect(Collectors.toList()));
    }

    public boolean validlyAnsweredForm(AnswerForm form) {
        List<TernaryAnswer> regular = tryConvertAnswers(form.getRegularanswers());
        List<TernaryAnswer> special = tryConvertAnswers(form.getSpecialanswers());
        return validAnswers(regular, realWorld.getNumberOfRegularQuestions(), config.getMinDisagreementsRegular())
                || validAnswers(special, realWorld.getNumberOfVotes(), config.getMinDisagreementsSpecial());
    }

    private List<TernaryAnswer> tryConvertAnswers(List<String> input) {
        List<TernaryAnswer> converted = Collections.emptyList();
        try {
            converted = input.stream().map(User::convertAnswer).collect(Collectors.toList());
        } catch (NullPointerException ignored) { }
        return converted;
    }

    public boolean userHasValidlyAnswered(User user) {
        return validAnswers(user.getRegularanswers(), realWorld.getNumberOfRegularQuestions(), config.getMinDisagreementsRegular())
        || validAnswers(user.getSpecialanswers(), realWorld.getNumberOfVotes(), config.getMinDisagreementsSpecial());
    }

    private boolean validAnswers(List<TernaryAnswer> answers, int nrAnswers, int requiredDisagreements) {
        return answers.size() == nrAnswers
                && meaningfulAnswers(answers) >= requiredDisagreements
                && !answers.contains(null);
    }

    private int meaningfulAnswers(List<TernaryAnswer> questions) {
        return (int) questions.stream().filter(q -> q != TernaryAnswer.DONTCARE).count();
    }

    public AnswerForm newFormFromUser(User user) {
        AnswerForm form = new AnswerForm();
        form.setMotivation(user.getMotivation());
        form.setLanguages(user.getLanguages());
        form.setLocations(user.locationString());
        form.setRegularanswers(user.getRegularAnswersAsListOfStrings());
        form.setSpecialanswers(user.getSpecialAnswersAsListOfStrings());
        return form;
    }

}
