package radius.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import radius.web.components.EmailProperties;

@Configuration
public class EmailConfig {
	
	@Autowired
	private EmailProperties p;
	
    @Bean
    public JavaMailSender getJavaMailSender() {
	
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(p.getHost());
        mailSender.setPort(p.getPort());
        mailSender.setUsername(p.getUser());
        mailSender.setPassword(p.getPass());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.ssl.enable", "true");
        props.put("mail.smtp.starttls.enable", "false");
        props.put("mail.debug", "false");
        props.put("mail.smtp.socketFactory.port", p.getPort());
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
         
        return mailSender;
    }

}
