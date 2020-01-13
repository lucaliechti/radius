package radius.data.form;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class MentionForm {

    @NotEmpty(message="{error.notBlank}")
    private String medium;

    @NotNull(message="{error.notBlank}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date date;

    @NotEmpty(message="{error.notBlank}")
    private String link;

    @NotEmpty(message="{error.notBlank}")
    private String type;

    @NotEmpty(message="{error.notBlank}")
    private String language;

}
