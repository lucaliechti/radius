package radius.web.components;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;

public interface EmailService {

//	@Async
//	public void sendSimpleMessage(String account, String[] recipients, String subject, String message);
//
//	@Async
//	public void sendSimpleMessage(String account, String recipient, String subject, String message);
	
	@Async
	public void sendSimpleMessage(String recipient, String subject, String message, JavaMailSenderImpl mailSender);
	
}
