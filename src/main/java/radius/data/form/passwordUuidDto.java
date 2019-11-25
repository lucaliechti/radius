package radius.data.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
public class passwordUuidDto {

    @Size(min=8, message="{error.sizePW}")
    private String password;

    private String uuid;

}
