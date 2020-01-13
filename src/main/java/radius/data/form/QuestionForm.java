package radius.data.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class QuestionForm {

    @NotEmpty(message="{error.atLeastOne}")
    private List<List<String>> regularQuestions;
    private List<List<String>> specialQuestions;

}
