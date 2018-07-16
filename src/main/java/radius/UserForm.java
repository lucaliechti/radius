package radius;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

public class UserForm {
	@Email(message="{error.email}")
	private String email;
	
	@Size(min=8, message="{error.sizePW}")
	private String password;
	
	public String getEmail() {
		return email;
	}
	public String getPassword() {
		return password;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
