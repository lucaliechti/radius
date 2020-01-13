package radius.data.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class ConfigurationForm {

    @NotNull(message="{error.notBlank}")
    private boolean matchingFactorWaitingTime;
    @NotNull(message="{error.notBlank}")
    private boolean matchingActive;
    @NotNull(message="{error.notBlank}")
    private int matchingMinimumDisagreementsRegular;
    @NotNull(message="{error.notBlank}")
    private int matchingMinimumDisagreementsSpecial;

    @NotNull(message="{error.notBlank}")
    private boolean specialActive;
    @NotNull(message="{error.notBlank}")
    private int numberOfVotes;
    @NotNull(message="{error.notBlank}")
    private int numberOfRegularQuestions;
    @NotEmpty(message="{error.notBlank}")
    private String currentVote;

}
