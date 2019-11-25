package radius.data.form;

import lombok.Getter;
import lombok.Setter;
import radius.validation.Conditional;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Conditional(selected = "newsletter", values = {"true"}, required = {}, email = {"emailN"}, pw = {})
@Conditional(selected = "registration", values = {"true"}, required = {"firstName", "lastName"}, email = {"emailR"}, pw = {"password"})
public class SurveyForm {

    @Size(min=1, max=10, message="{error.max10}")
    private List<Integer> questions;

    private List<String> answers = new ArrayList<String>();

    //Newsletter
    private Boolean newsletter;
    private String emailN;

    //Registration
    private Boolean registration;
    private String emailR;
    private String password;
    private String firstName;
    private String lastName;
    private String canton;

}
