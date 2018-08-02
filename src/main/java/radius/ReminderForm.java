package radius;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class ReminderForm {
	
	@NotEmpty(message="{error.email}")
	@Email(message="{error.email}")
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
