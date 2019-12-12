package radius.web.service;

import org.springframework.stereotype.Service;
import radius.User;
import radius.User.TernaryAnswer;
import radius.data.form.AnswerForm;
import radius.web.components.RealWorldProperties;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnswerService {

    private RealWorldProperties realWorld;
    private static final int MIN_DISAGREEMENTS_REGULAR = 2;
    private static final int MIN_DISAGREEMENTS_SPECIAL = 1;

    public AnswerService(RealWorldProperties realWorld) {
        this.realWorld = realWorld;
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
        List<TernaryAnswer> regular = form.getRegularanswers().stream().map(User::convertAnswer).collect(Collectors.toList());
        List<TernaryAnswer> special = form.getSpecialanswers().stream().map(User::convertAnswer).collect(Collectors.toList());
        return validAnswers(regular, realWorld.getNumberOfRegularQuestions(), MIN_DISAGREEMENTS_REGULAR)
                || validAnswers(special, realWorld.getNumberOfVotes(), MIN_DISAGREEMENTS_SPECIAL);
    }

    public boolean userHasValidlyAnswered(User user) {
        return validAnswers(user.getRegularanswers(), realWorld.getNumberOfRegularQuestions(), MIN_DISAGREEMENTS_REGULAR)
        || validAnswers(user.getSpecialanswers(), realWorld.getNumberOfVotes(), MIN_DISAGREEMENTS_SPECIAL);
    }

    private boolean validAnswers(List<TernaryAnswer> answers, int nrAnswers, int requiredDisagreements) {
        return answers.size() == nrAnswers && meaningfulAnswers(answers) >= requiredDisagreements;
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
