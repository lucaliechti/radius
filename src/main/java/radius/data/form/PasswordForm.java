package radius.data.form;

import javax.validation.constraints.Size;

public class PasswordForm {

    @Size(min=8, message="{error.sizePW}")
    private String password;

    private String uuid;

    //getters and setters
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
