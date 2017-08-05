package reach.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DataConfig {
	
	@Bean
	public DataSource datasource() {
		DriverManagerDataSource datasource = new DriverManagerDataSource();
		datasource.setDriverClassName("org.postgresql.Driver");
		datasource.setUrl("jdbc:postgresql://localhost:5432/reach");
		datasource.setUsername("postgres");
		datasource.setPassword("admin");
		return datasource;
	}
}
