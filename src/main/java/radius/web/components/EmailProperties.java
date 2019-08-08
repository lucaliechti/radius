package radius.web.components;

import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;

//import org.springframework.stereotype.Component;

public class EmailProperties {
	
	private String host;
	private int port;
	private String user;
	private String pass;
	
	public EmailProperties(String file) {
		readFromFile(file);
	}
	
	private void readFromFile(String file) {
		Properties p = new Properties();
		InputStream in;
		try {
//			in = (BufferedInputStream) EmailProperties.class.getResourceAsStream("/config/email.properties");
			in = EmailProperties.class.getResourceAsStream(file);
			p.load(in);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		host = p.getProperty("host");
		try {
			port = Integer.parseInt(p.getProperty("port"));
		}
		catch (NumberFormatException nfe) { System.out.println("You have to specify an integer type host number"); }
		user = p.getProperty("user");
		pass = p.getProperty("pass");
	}

	public String getHost() {
		return host;
	}
	
	public int getPort() {
		return port;
	}
	
	public String getUser() {
		return user;
	}
	
	public String getPass() {
		return pass;
	}
}
