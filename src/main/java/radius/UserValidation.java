package radius;

//LL: DTO used to validate the identity of a user. Used for confirming the email address
//and unsubscribing from the newsletter.
public class UserValidation {

    private String email;
    private String uuid;

    public UserValidation(String _email, String _uuid) {
        this.email = _email;
        this.uuid = _uuid;
    }

    public String getEmail() { return email; }
    public void setEmail(String _email) { email = _email; }

    public String getUuid() { return uuid; }
    public void setUuid(String _uuid) { uuid = _uuid; }

}
