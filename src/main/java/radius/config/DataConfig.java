package radius.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import radius.web.components.ProfileDependentProperties;

import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class DataConfig {
	
	@Bean
	public BasicDataSource dataSource(ProfileDependentProperties prop) {
		Properties dbProperties = new Properties();
		String dbFile = prop.getDbfile();
		try (InputStream in = DataConfig.class.getResourceAsStream(dbFile)) {
			dbProperties.load(in);
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
