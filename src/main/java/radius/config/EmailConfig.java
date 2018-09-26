package radius.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import radius.web.components.EmailProperties;

@Configuration
public class EmailConfig {
	
    @Bean
    public JavaMailSenderImpl helloMailSender() {
    	
    	EmailProperties p = new EmailProperties("/config/email_hello.properties");
    	JavaMailSenderImpl mailSender = createFromProperties(p);
        return mailSender;
    }
    
    @Bean
    public JavaMailSenderImpl matchingMailSender() {
    	
    	EmailProperties p = new EmailProperties("/config/email_matching.properties");
    	JavaMailSenderImpl mailSender = createFromProperties(p);
        return mailSender;
    }

	private JavaMailSenderImpl createFromProperties(EmailProperties p) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(p.getHost());
        mailSender.setPort(p.getPort());
        mailSender.setUsername(p.getUser());
        mailSender.setPassword(p.getPass());
        mailSender.setDefaultEncoding("UTF-8");

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