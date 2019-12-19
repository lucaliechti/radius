package radius.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
@AllArgsConstructor
public class EmailSourceDto {

    @NotEmpty
    private String email;

    @NotEmpty
    private String source;

}
