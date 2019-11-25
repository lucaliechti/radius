package radius.data.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
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

}
