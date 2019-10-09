package radius.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import radius.web.components.ProfileConfiguration;

import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class DataConfig {
	
	@Bean
	public BasicDataSource datasource(ProfileConfiguration.ProfileProperties profileProp) {
		Properties dbProperties = new Properties();
		InputStream in;
		try {
			in = DataConfig.class.getResourceAsStream(profileProp.getDbfile());
			dbProperties.load(in);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		BasicDataSource datasource = new BasicDataSource();
		datasource.setDriverClassName(dbProperties.getProperty("driver"));
		datasource.setUrl(dbProperties.getProperty("url"));
		datasource.setUsername(dbProperties.getProperty("user"));
		datasource.setPassword(dbProperties.getProperty("pass"));
		datasource.setInitialSize(5);
		datasource.setMaxActive(10);
		return datasource;
	}
}
