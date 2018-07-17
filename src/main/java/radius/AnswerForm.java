package radius;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class AnswerForm {

	private String motivation = null;
	
	@NotNull(message="{error.question}")
	private Boolean q1;

	@NotNull(message="{error.question}")
	private Boolean q2;
	
	@NotNull(message="{error.question}")
	private Boolean q3;
	
	@NotNull(message="{error.question}")
	private Boolean q4;
	
	@NotNull(message="{error.question}")
	private Boolean q5;

	@NotEmpty(message="{error.notBlank}")
	private String modus;
	
	@NotEmpty(message="{error.atLeastOne}")
	private List<String> languages = new ArrayList<String>();
	
	private List<Integer> locations = new ArrayList<Integer>();
	
	public String getMotivation() {
		return motivation;
	}

	public Boolean getQ1() {
		return q1;
	}

	public Boolean getQ2() {
		return q2;
	}

	public Boolean getQ3() {
		return q3;
	}

	public Boolean getQ4() {
		return q4;
	}

	public Boolean getQ5() {
		return q5;
	}

	public String getModus() {
		return modus;
	}

	public List<String> getLanguages() {
		return languages;
	}

	public List<Integer> getLocations() {
		return locations;
	}

	public void setMotivation(String motivation) {
		this.motivation = motivation;
	}

	public void setQ1(Boolean q1) {
		this.q1 = q1;
	}

	public void setQ2(Boolean q2) {
		this.q2 = q2;
	}

	public void setQ3(Boolean q3) {
		this.q3 = q3;
	}

	public void setQ4(Boolean q4) {
		this.q4 = q4;
	}

	public void setQ5(Boolean q5) {
		this.q5 = q5;
	}

	public void setModus(String modus) {
		this.modus = modus;
	}

	public void setLanguages(List<String> languages) {
		this.languages = languages;
	}

	public void setLocations(List<Integer> locations) {
		this.locations = locations;
	}
}

