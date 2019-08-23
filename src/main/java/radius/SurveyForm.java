package radius;

import radius.web.validation.Conditional;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Conditional(selected = "newsletter", values = {"true"}, required = {}, email = {"emailN"}, pw = {})
@Conditional(selected = "registration", values = {"true"}, required = {"firstName", "lastName"}, email = {"emailR"}, pw = {"password"})
public class SurveyForm {

    @Size(min=1, max=10, message="{error.max10}")
    private List<Integer> questions;

    private Boolean a1;
    private Boolean a2;
    private Boolean a3;
    private Boolean a4;
    private Boolean a5;
    private Boolean a6;
    private Boolean a7;
    private Boolean a8;
    private Boolean a9;
    private Boolean a10;
    private Boolean a11;
    private Boolean a12;
    private Boolean a13;
    private Boolean a14;
    private Boolean a15;

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

    //Answer getters
    public Boolean getA1() { return a1; }
    public Boolean getA2() { return a2; }
    public Boolean getA3() { return a3; }
    public Boolean getA4() { return a4; }
    public Boolean getA5() { return a5; }
    public Boolean getA6() { return a6; }
    public Boolean getA7() { return a7; }
    public Boolean getA8() { return a8; }
    public Boolean getA9() { return a9; }
    public Boolean getA10() { return a10; }
    public Boolean getA11() { return a11; }
    public Boolean getA12() { return a12; }
    public Boolean getA13() { return a13; }
    public Boolean getA14() { return a14; }
    public Boolean getA15() { return a15; }

    //Answer setters
    public void setA1(Boolean q) { this.a1 = q; }
    public void setA2(Boolean q) { this.a2 = q; }
    public void setA3(Boolean q) { this.a3 = q; }
    public void setA4(Boolean q) { this.a4 = q; }
    public void setA5(Boolean q) { this.a5 = q; }
    public void setA6(Boolean q) { this.a6 = q; }
    public void setA7(Boolean q) { this.a7 = q; }
    public void setA8(Boolean q) { this.a8 = q; }
    public void setA9(Boolean q) { this.a9 = q; }
    public void setA10(Boolean q) { this.a10 = q; }
    public void setA11(Boolean q) { this.a11 = q; }
    public void setA12(Boolean q) { this.a12 = q; }
    public void setA13(Boolean q) { this.a13 = q; }
    public void setA14(Boolean q) { this.a14 = q; }
    public void setA15(Boolean q) { this.a15 = q; }

    //returns an ordered list of answers; answers may be null
    public List<Boolean> getAnswers() {
        ArrayList<Boolean> answers = new ArrayList<Boolean>();
        answers.add(a1);
        answers.add(a2);
        answers.add(a3);
        answers.add(a4);
        answers.add(a5);
        answers.add(a6);
        answers.add(a7);
        answers.add(a8);
        answers.add(a9);
        answers.add(a10);
        answers.add(a11);
        answers.add(a12);
        answers.add(a13);
        answers.add(a14);
        answers.add(a15);
        return answers;
    }

    //Newsletter getters/setters
    public Boolean getNewsletter() { return newsletter; }
    public String getEmailN() { return emailN; }
    public void setNewsletter(Boolean b) { this.newsletter = b; }
    public void setEmailN(String email) {
        this.emailN = email;
    }

    //Registration getters/setters
    public Boolean getRegistration() { return registration; }
    public void setRegistration(Boolean b) { this.registration = b; }
    public String getEmailR() { return emailR; }
    public String getPassword() {
        return password;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getCanton() {
        return canton;
    }
    public void setEmailR(String email) {
        this.emailR = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setCanton(String canton) {
        this.canton = canton;
    }

}
