package radius.config;

import java.util.Locale;

import javax.validation.Validator;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan("radius.web")
public class WebConfig extends WebMvcConfigurerAdapter {
	
	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		resolver.setExposeContextBeansAsAttributes(true);
		return resolver;
	}
	
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
	
	//i18n: http://www.concretepage.com/spring-4/spring-4-mvc-internationalization-i18n-
	//	and-localization-l10n-annotation-example
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}
	
	@Bean 
	public LocaleChangeInterceptor localeChangeInterceptor(){
	    LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
	    localeChangeInterceptor.setParamName("lang");
	    return localeChangeInterceptor;
	}
	
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.addBasenames("classpath:messages");
        //messageSource.addBasenames("classpath:validation");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
    
    @Bean
    public LocaleResolver localeResolver(){
    	CookieLocaleResolver resolver = new CookieLocaleResolver();
    	resolver.setDefaultLocale(new Locale("de"));
//    	resolver.setCookieName("radiusLocale");
//    	resolver.setCookieMaxAge(3600);
    	return resolver;
    }
    
    //display dateFormats according to locale:
    //http://docs.oracle.com/javaee/5/jstl/1.1/docs/tlddocs/fmt/formatDate.html
    //https://docs.oracle.com/javase/tutorial/i18n/format/dateFormat.html
    //e.g. <fmt:formatDate value="${date}" type="both" dateStyle="full" timeStyle="short" />
    
    @Bean
    public Validator validator() {
    	LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
    	validator.setValidationMessageSource(messageSource());
    	return validator;
    }
    
}
