package radius.data.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class EmailDto {

    @NotEmpty(message="error.notBlank")
    @Email(message="{error.email}")
    private String email;

}
