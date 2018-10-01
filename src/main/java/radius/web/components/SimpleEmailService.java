package radius.web.components;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Component
public class SimpleEmailService implements EmailService {
	
    public JavaMailSender emailSender;

	@Override
	public void sendSimpleMessage(String recipient, String subject, String message, JavaMailSenderImpl mailSender) {
		SimpleMailMessage email = new SimpleMailMessage();
		email.setFrom(mailSender.getUsername());
		email.setTo(recipient);
		email.setSubject(subject);
		email.setText(message);
		mailSender.send(email);
	}
}
