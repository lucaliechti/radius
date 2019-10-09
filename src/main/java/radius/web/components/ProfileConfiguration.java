package radius.web.components;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:config/profile.properties")
public class ProfileConfiguration {

    @Autowired
    Environment env;

    @Bean
    public ProfileProperties profileDependentProperties() {
        String dbfile = env.getProperty("db.conf.file");

        return new ProfileProperties(dbfile);
    }

    @AllArgsConstructor
    @Getter
    public static class ProfileProperties {
        private String dbfile;
    }
}
