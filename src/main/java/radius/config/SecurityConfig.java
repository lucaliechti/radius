package radius.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import radius.security.CustomAuthenticationProvider;

import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//authorization
		http
			.authorizeRequests()
				.antMatchers("/history", "/myexperience", "/profile", "/answers", "/status").authenticated()
				.antMatchers("/monitoring/**", "/admin/**").hasRole("ADMIN")
				.antMatchers(HttpMethod.POST, "/experience").authenticated()
				.anyRequest().permitAll()
				;//.and().requiresChannel().antMatchers("/login", "/register").requiresSecure(); //what else?
		
		//TODO: Custom login handler
		//login
		http
			.formLogin().loginPage("/login")
				.defaultSuccessUrl("/status?login")
				.failureUrl("/status?error")
				.and().rememberMe().tokenValiditySeconds(2419200).key("RadiusId").userDetailsService(userDetailsService) //four weeks
				.and().csrf();
		
		
		//TODO: Custom logout handler
		//logout
		http
			.logout()
				.logoutUrl("/logout")
				.deleteCookies("JSESSIONID")
				.logoutSuccessUrl("/home?logout");
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
	
    @Bean
    public DaoAuthenticationProvider authProvider() {
    	final CustomAuthenticationProvider authProvider = new CustomAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        return authProvider;
    }

    
}