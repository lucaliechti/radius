package radius.web.components;

public interface EmailService {

	public void sendSimpleMessage(String account, String[] recipients, String subject, String message);
	
	public void sendSimpleMessage(String account, String recipient, String subject, String message);
	
}
