package radius.data.form;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;

public class AnswerForm {

	private String motivation = null;

	@NotEmpty(message="{error.notBlank}")
	private List<String> regularanswers = new ArrayList<String>();

	@NotEmpty(message="{error.notBlank}")
	private List<String> specialanswers = new ArrayList<String>();

	@NotEmpty(message="{error.notBlank}")
	private String modus;
	
	@NotEmpty(message="{error.atLeastOne}")
	private List<String> languages = new ArrayList<String>();
	
	@NotEmpty(message="{error.atLeastOne}")
	private String locations;
	
	public String getMotivation() { return motivation; }

	public List<String> getRegularanswers() { return regularanswers; }

	public void setRegularanswers(List<String> input) { regularanswers = input; }

	public List<String> getSpecialanswers() { return specialanswers; }

	public void setSpecialanswers(List<String> input) { specialanswers = input; }

	public String getModus() { return modus; }

	public List<String> getLanguages() { return languages; }

	public String getLocations() { return locations; }

	public void setMotivation(String motivation) { this.motivation = motivation; }

	public void setModus(String modus) { this.modus = modus; }

	public void setLanguages(List<String> languages) { this.languages = languages; }

	public void setLocations(String locations) { this.locations = locations; }
}

