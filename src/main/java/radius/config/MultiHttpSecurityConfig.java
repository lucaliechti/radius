package radius.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import radius.security.CustomAuthenticationProvider;
import radius.security.LoginTimeAwareAuthenticationSuccessHandler;

@PropertySource("classpath:config/profile.properties")
@EnableWebSecurity
public class MultiHttpSecurityConfig {

	@Configuration
	public static class FormLoginConfiguration extends WebSecurityConfigurerAdapter {

		@Value("${security.requireSSL}")
		private boolean requireSSL;

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.authenticationProvider(authProvider());
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
				.authorizeRequests()
				.antMatchers("/profile", "/answers", "/status", "/toggleStatus").authenticated()
				.antMatchers("/admin/**", "/updateConfiguration/**", "/contactUsers/**",
						"/sendNewsletter/**", "/banUser**", "/deleteUser**", "/setPrivate**", "/unsubscribeNewsletter**",
						"/actuator/**", "/health/**").hasRole("ADMIN")
				.anyRequest().permitAll();

			http
				.formLogin().loginPage("/home")
				.defaultSuccessUrl("/status")
				.failureUrl("/home?error")
				.successHandler(handler)
				.and().rememberMe().tokenValiditySeconds(2419200).key("remember-me").userDetailsService(userDetailsService) //four weeks
				.and().csrf();

			http
				.logout()
					.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));

			if(requireSSL) {
				http
					.requiresChannel()
						.anyRequest().requiresSecure();
			}
		}

		@Qualifier("postgresUserDetailsService")
		@Autowired
		private UserDetailsService userDetailsService;

		@Qualifier("loginTimeAwareAuthenticationSuccessHandler")
		@Autowired
		private LoginTimeAwareAuthenticationSuccessHandler handler;

		@Bean
		DaoAuthenticationProvider authProvider() {
			final CustomAuthenticationProvider authProvider = new CustomAuthenticationProvider();
			authProvider.setUserDetailsService(userDetailsService);
			return authProvider;
		}
	}

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
