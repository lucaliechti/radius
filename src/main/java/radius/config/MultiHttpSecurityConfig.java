package radius.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import radius.security.CustomAuthenticationProvider;

@EnableWebSecurity
public class MultiHttpSecurityConfig {
	@Configuration
	public static class FormLoginConfiguration extends WebSecurityConfigurerAdapter {
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
					.antMatchers("/api/**").denyAll()
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

		@Autowired
		private UserDetailsService userDetailsService;

		@Bean
		DaoAuthenticationProvider authProvider() {
			final CustomAuthenticationProvider authProvider = new CustomAuthenticationProvider();
			authProvider.setUserDetailsService(userDetailsService);
			return authProvider;
		}


	}

	@Configuration
	@PropertySource("classpath:config/matcher.properties")
	@Order(1)
	public static class ApiConfiguration extends WebSecurityConfigurerAdapter {
		@Autowired
		Environment env;

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.inMemoryAuthentication()
					.passwordEncoder(encoder())
					.withUser(env.getProperty("matcher.user")).password(encoder().encode(env.getProperty("matcher.pass"))).roles("MATCHER");
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.antMatcher("/api/**")
					.authorizeRequests()
					.anyRequest().hasRole("MATCHER").and()
					.httpBasic().and()
					.csrf().disable()
					.requiresChannel().anyRequest().requiresSecure();
		}
	}

	@Bean
	public static PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
}