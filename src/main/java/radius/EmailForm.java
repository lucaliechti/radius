package radius;

import javax.validation.constraints.Email;

public class EmailForm {
    @Email(message="{error.email}")
    private String email;

    //getters and setters
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

}
