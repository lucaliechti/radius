package radius.data.form;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class AnswerForm {

	private String motivation;
	private List<String> regularanswers = new ArrayList<>();
	private List<String> specialanswers = new ArrayList<>();

	@NotEmpty(message="{error.atLeastOne}")
	private List<String> languages = new ArrayList<>();
	
	@NotEmpty(message="{error.atLeastOne}")
	private String locations;
	
}

