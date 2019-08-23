package radius;

import javax.validation.constraints.NotEmpty;

public class NewsletterMessage {
    @NotEmpty
    private String subject;

    @NotEmpty
    private String message;

    public String getSubject() { return subject; }
    public void setSubject(String _subject) { subject = _subject; }

    public String getMessage() { return message; }
    public void setMessage(String _message) { message = _message; }
}
