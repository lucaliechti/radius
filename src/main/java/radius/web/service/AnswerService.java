package radius.web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import radius.User;
import radius.User.TernaryAnswer;
import radius.data.form.AnswerForm;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AnswerService {

    private final ConfigService configService;

    public void updateUserFromAnswerForm(User user, AnswerForm answerForm) {
        user.setStatus(User.UserStatus.WAITING);
        user.setLanguages(answerForm.getLanguages());
        user.setMotivation(answerForm.getMotivation());
        try {
            user.setRegularanswers(answerForm.getRegularanswers());
        } catch (NullPointerException npe) { user.setRegularanswers(Collections.emptyList()); }
        try {
            user.setSpecialanswers(answerForm.getSpecialanswers());
        } catch (NullPointerException npe) { user.setSpecialanswers(Collections.emptyList()); }
        user.setLocations(Arrays.stream(answerForm.getLocations().split(";")).map(Integer::valueOf)
                .collect(Collectors.toList()));
    }

    public boolean validlyAnsweredForm(AnswerForm form) {
        List<TernaryAnswer> regular = tryConvertAnswers(form.getRegularanswers());
        List<TernaryAnswer> special = tryConvertAnswers(form.getSpecialanswers());
        return validAnswers(regular, configService.numberOfRegularQuestions(), configService.matchingMinimumDisagreementsRegular())
                || validAnswers(special, configService.numberOfVotes(), configService.matchingMinimumDisagreementsSpecial());
    }

    private List<TernaryAnswer> tryConvertAnswers(List<String> input) {
        List<TernaryAnswer> converted = Collections.emptyList();
        try {
            converted = input.stream().map(User::convertAnswer).collect(Collectors.toList());
        } catch (NullPointerException ignored) { }
        return converted;
    }

    public boolean userHasValidlyAnswered(User user) {
        return validAnswers(user.getRegularanswers(), configService.numberOfRegularQuestions(), configService.matchingMinimumDisagreementsRegular())
        || validAnswers(user.getSpecialanswers(), configService.numberOfVotes(), configService.matchingMinimumDisagreementsSpecial());
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
        form.setRegularanswers(user.getRegularanswers().stream().map(User::convertAnswerToString)
                .collect(Collectors.toList()));
        form.setSpecialanswers(user.getSpecialanswers().stream().map(User::convertAnswerToString)
                .collect(Collectors.toList()));
        return form;
    }

}
