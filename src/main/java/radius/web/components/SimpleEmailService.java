package radius.web.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class SimpleEmailService implements EmailService {
	
	@Autowired
    public JavaMailSender emailSender;
	
	@Autowired
	private EmailProperties p;

	@Override
	public void sendSimpleMessage(String[] recipients, String subject, String message) {
		SimpleMailMessage email = new SimpleMailMessage();
		email.setFrom(p.getUser());
		email.setTo(recipients);
		email.setSubject(subject);
		email.setText(message);
        emailSender.send(email);
	}

	@Override
	public void sendSimpleMessage(String recipient, String subject, String message) {
		SimpleMailMessage email = new SimpleMailMessage();
		email.setFrom(p.getUser());
		email.setTo(recipient);
		email.setSubject(subject);
		email.setText(message);
        emailSender.send(email);
	}
}
