package radius.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import radius.web.components.ProfileDependentProperties;

@Configuration
@PropertySource("classpath:config/database.properties")
public class DataConfig {

	@Value("${dev.driver}")
	private String devDriver;
	@Value("${dev.url}")
	private String devUrl;
	@Value("${dev.user}")
	private String devUser;
	@Value("${dev.pass}")
	private String devPass;

	@Value("${prod.driver}")
	private String prodDriver;
	@Value("${prod.url}")
	private String prodUrl;
	@Value("${prod.user}")
	private String prodUser;
	@Value("${prod.pass}")
	private String prodPass;
	
	@Bean
	public BasicDataSource dataSource(ProfileDependentProperties prop) {
		BasicDataSource datasource = new BasicDataSource();
		if("prod".equals(prop.getEnvironment())) {
			datasource.setDriverClassName(prodDriver);
			datasource.setUrl(prodUrl);
			datasource.setUsername(prodUser);
			datasource.setPassword(prodPass);
		} else {
			datasource.setDriverClassName(devDriver);
			datasource.setUrl(devUrl);
			datasource.setUsername(devUser);
			datasource.setPassword(devPass);
		}
		datasource.setInitialSize(5);
		datasource.setMaxActive(10);
		return datasource;
	}
}
