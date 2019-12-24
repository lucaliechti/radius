package radius.web.components;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Component
@PropertySource("classpath:config/profile.properties")
public class ProfileDependentProperties {

    @Value("${db.conf.file}")
    String dbfile;

    @Value("${radius.url}")
    String url;

    @Value("${matching.sendemails}")
    boolean sendEmails;

}
