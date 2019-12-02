package radius.data.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class PasswordUuidDto {

    @Size(min=8, message="{error.sizePW}")
    private String password;

    @NotEmpty
    private String uuid;

}
