package radius.exceptions;

public class EmailAlreadyExistsException extends Exception {
    
	public EmailAlreadyExistsException() {}

    public EmailAlreadyExistsException(String message) {
       super(message);
    }
}
