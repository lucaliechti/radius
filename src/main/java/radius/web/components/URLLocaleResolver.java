package radius.web.components;

import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

public class URLLocaleResolver implements LocaleResolver {

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        URL url;
        try {
            url = new URL(request.getRequestURL().toString());
        } catch (MalformedURLException murle) {
            return new Locale("de");
        }
        switch(url.getHost()) {
            case "radius-schweiz.ch":
                return new Locale("de");
            case "radius-suisse.ch":
                return new Locale("fr");
            case "radius-svizzera.ch":
                return new Locale("de");
            default:
                return new Locale("de");
        }
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) { }
}
