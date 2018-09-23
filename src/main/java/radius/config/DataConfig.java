package radius.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Properties;

//import javax.sql.DataSource;

@Configuration
public class DataConfig {
	
	@Bean
	public BasicDataSource datasource() {
		Properties dbProperties = new Properties();
		BufferedInputStream in;
		try {
			in = (BufferedInputStream) DataConfig.class.getResourceAsStream("/config/database_prod.properties");
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
