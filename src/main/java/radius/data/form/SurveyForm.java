package radius.data.form;

import radius.validation.Conditional;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

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

    //Questions getter/setter
    public List<Integer> getQuestions() { return questions; }
    public void setQuestions(List<Integer> q) { questions = q; }

    //Answers getter/setter
    public List<String> getAnswers() { return answers; }
    public void setAnswers(List<String> a) { this.answers = a; }

    //Newsletter getters/setters
    public Boolean getNewsletter() { return newsletter; }
    public String getEmailN() { return emailN; }
    public void setNewsletter(Boolean b) { this.newsletter = b; }
    public void setEmailN(String email) { this.emailN = email; }

    //Registration getters/setters
    public Boolean getRegistration() { return registration; }
    public void setRegistration(Boolean b) { this.registration = b; }
    public String getEmailR() { return emailR; }
    public String getPassword() { return password; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getCanton() { return canton; }
    public void setEmailR(String email) { this.emailR = email; }
    public void setPassword(String password) { this.password = password; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setCanton(String canton) { this.canton = canton; }

}
