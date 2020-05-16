package radius.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@PropertySource("classpath:config/email.properties")
public class EmailConfig {

    @Value("${email.host}")
    private String host;
    @Value("${email.port}")
    private int port;

    @Value("${email.hello.user}")
    private String helloUser;
    @Value("${email.hello.pass}")
    private String helloPass;

    @Value("${email.matching.user}")
    private String matchingUser;
    @Value("${email.matching.pass}")
    private String matchingPass;

    @Value("${email.newsletter.user}")
    private String newsletterUser;
    @Value("${email.newsletter.pass}")
    private String newsletterPass;

    @Bean
    public JavaMailSenderImpl helloMailSender() {
        return createMailSender(host, port, helloUser, helloPass);
    }
    
    @Bean
    public JavaMailSenderImpl matchingMailSender() {
        return createMailSender(host, port, matchingUser, matchingPass);
    }

    @Bean
    public JavaMailSenderImpl newsletterMailSender() {
        return createMailSender(host, port, newsletterUser, newsletterPass);
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
        props.put("mail.ssl.enable", "false");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "false");
        props.put("mail.smtp.socketFactory.port", port);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
         
        return mailSender;
	}

}
