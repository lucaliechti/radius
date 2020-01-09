package radius.web.components;

import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public class CompositeLocaleResolver implements LocaleResolver {

    private URLLocaleResolver urlResolver;
    private CookieLocaleResolver cookieResolver;

    public CompositeLocaleResolver() {
        this.urlResolver = new URLLocaleResolver();

    	CookieLocaleResolver cResolver = new CookieLocaleResolver();
        cResolver.setCookieName("radiusLocale");
        cResolver.setCookieMaxAge(2419200); //four weeks
        this.cookieResolver = cResolver;
    }

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        this.cookieResolver.setDefaultLocale(urlResolver.resolveLocale(request));
        return this.cookieResolver.resolveLocale(request);
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        cookieResolver.setLocale(request, response, locale);
    }
}
