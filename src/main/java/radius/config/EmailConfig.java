package radius.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@PropertySource("classpath:config/email.properties")
public class EmailConfig {

    @Value("${email.host}")
    private String host;

    @Value("${email.port}")
    private int port;

    private Environment env;

    public EmailConfig(Environment env) {
        this.env = env;
    }
	
    @Bean
    public JavaMailSenderImpl helloMailSender() {
        String user = env.getProperty("email.hello.user");
        String pass = env.getProperty("email.hello.pass");
    	JavaMailSenderImpl mailSender = createMailSender(host, port, user, pass);
        return mailSender;
    }
    
    @Bean
    public JavaMailSenderImpl matchingMailSender() {
        String user = env.getProperty("email.matching.user");
        String pass = env.getProperty("email.matching.pass");
        JavaMailSenderImpl mailSender = createMailSender(host, port, user, pass);
        return mailSender;
    }

    @Bean
    public JavaMailSenderImpl newsletterMailSender() {
        String user = env.getProperty("email.newsletter.user");
        String pass = env.getProperty("email.newsletter.pass");
        JavaMailSenderImpl mailSender = createMailSender(host, port, user, pass);
        return mailSender;
    }

	private JavaMailSenderImpl createMailSender(String host, int port, String user, String pass) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(user);
        mailSender.setPassword(pass);
        mailSender.setDefaultEncoding("UTF-8");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.ssl.enable", "true");
        props.put("mail.smtp.starttls.enable", "false");
        props.put("mail.debug", "false");
        props.put("mail.smtp.socketFactory.port", port);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
         
        return mailSender;
	}

}
