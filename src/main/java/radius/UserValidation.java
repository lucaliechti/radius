package radius;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

//LL: DTO used to validate the identity of a user. Used for confirming the email address
//and unsubscribing from the newsletter.
@AllArgsConstructor
@Data
public class UserValidation {

    @NotEmpty
    private String email;

    @NotEmpty
    private String uuid;

}
