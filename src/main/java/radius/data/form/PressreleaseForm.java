package radius.data.form;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class PressreleaseForm {

    @NotNull(message="{error.notBlank}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date date;

    @NotNull(message="{error.notBlank}")
    private MultipartFile file_de;

    @NotNull(message="{error.notBlank}")
    private MultipartFile file_fr;

}
