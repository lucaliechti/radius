package reach.config;

//import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataConfig {
	
	@Bean
	public BasicDataSource datasource() {
		BasicDataSource datasource = new BasicDataSource();
		datasource.setDriverClassName("org.postgresql.Driver");
		datasource.setUrl("jdbc:postgresql://localhost:5432/reach");
		datasource.setUsername("postgres");
		datasource.setPassword("admin");
		datasource.setInitialSize(5);
		datasource.setMaxActive(10);
		return datasource;
	}
}
