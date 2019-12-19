package radius.data.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class NewsletterForm {

    @NotEmpty(message="{error.notBlank}")
    private String subject;

    @NotEmpty(message="{error.notBlank}")
    private String message;

    @NotEmpty(message="{error.atLeastOneRecipient}")
    private String recipients;

}
