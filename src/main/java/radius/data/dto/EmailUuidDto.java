package radius.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@Data
public class EmailUuidDto {

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    private String uuid;

}
