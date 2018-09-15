package radius.web.components;

import org.springframework.scheduling.annotation.Async;

public interface EmailService {

	@Async
	public void sendSimpleMessage(String[] recipients, String subject, String message);

	@Async
	public void sendSimpleMessage(String recipient, String subject, String message);
	
}
