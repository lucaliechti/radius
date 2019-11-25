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
	private List<String> regularanswers = new ArrayList<String>();
	private List<String> specialanswers = new ArrayList<String>();

	@NotEmpty(message="{error.notBlank}")
	private String modus;
	
	@NotEmpty(message="{error.atLeastOne}")
	private List<String> languages = new ArrayList<String>();
	
	@NotEmpty(message="{error.atLeastOne}")
	private String locations;
	
}

