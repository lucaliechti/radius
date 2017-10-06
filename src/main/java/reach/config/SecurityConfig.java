package reach.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	DataSource dataSource;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.jdbcAuthentication()
			.dataSource(dataSource)
			.usersByUsernameQuery("SELECT email AS principal, password AS credentials, enabled FROM users WHERE email=?")
			.authoritiesByUsernameQuery("SELECT email AS principal, authority AS role FROM authorities WHERE email=?")
			;//.passwordEncoder(new StandardPasswordEncoder("53cr3t"));
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//authorization
		http
			.authorizeRequests()
				.antMatchers("/history", "/myexperience", "/profile").authenticated()
				.antMatchers(HttpMethod.POST, "/experience").authenticated()
				.anyRequest().permitAll()
				;//.and().requiresChannel().antMatchers("/login", "/register").requiresSecure(); //what else?
		
		//login
		http
			.formLogin().loginPage("/login")
			.defaultSuccessUrl("/profile?login")
			.failureUrl("/login?error")
			.and().rememberMe().tokenValiditySeconds(2419200).key("ReachId") //four weeks
			.and().csrf();
		
		//logout
		http
			.logout()
				.logoutUrl("/logout")
				.logoutSuccessUrl("/home?logout");
	}
}