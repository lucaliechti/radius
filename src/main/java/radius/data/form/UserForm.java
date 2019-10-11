package radius.data.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


public class UserForm {
	@Email(message="{error.email}")
	private String email;
	
	@Size(min=8, message="{error.sizePW}")
	private String password;
	
	@NotEmpty(message="{error.notBlank}")
	private String firstName;
	
	@NotEmpty(message="{error.notBlank}")
	private String lastName;
	
	private String canton;
	
	
	//getters and setters
	public String getEmail() {
		return email;
	}
	public String getPassword() {
		return password;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getCanton() {
		return canton;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public void setCanton(String canton) {
		this.canton = canton;
	}
	
}
