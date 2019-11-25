package radius.data.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter
@Setter
public class EmailDto {

    @Email(message="{error.email}")
    private String email;

}
