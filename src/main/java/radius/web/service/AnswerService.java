package radius.web.service;

import org.springframework.stereotype.Service;
import radius.User;
import radius.data.form.AnswerForm;
import radius.web.components.RealWorldProperties;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnswerService {

    private RealWorldProperties realWorld;

    public AnswerService(RealWorldProperties realWorld) {
        this.realWorld = realWorld;
    }

    public void updateUserFromAnswerForm(User user, AnswerForm answerForm) {
        user.setStatus(User.UserStatus.WAITING);
        user.setAnsweredRegular(answerForm.getRegularanswers().size() > 0);
        user.setLanguages(answerForm.getLanguages());
        user.setMotivation(answerForm.getMotivation());
        user.setRegularanswers(answerForm.getRegularanswers());
        user.setSpecialanswers(answerForm.getSpecialanswers());
        user.setLocations(Arrays.stream(answerForm.getLocations().split(";")).map(Integer::valueOf)
                .collect(Collectors.toList()));
    }

    public boolean validlyAnswered(AnswerForm form) {
        return validAnswers(form.getRegularanswers(), realWorld.getNumberOfRegularQuestions())
                || validAnswers(form.getSpecialanswers(), realWorld.getNumberOfVotes());
    }

    private boolean validAnswers(List<String> answers, int nrAnswers) {
        return answers.size() == nrAnswers && (answers.contains("TRUE") || answers.contains("FALSE"));
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
