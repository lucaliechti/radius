package radius.web.components;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:config/profile.properties")
public class ProfileConfiguration {

    @Value("${db.conf.file}")
    String dbfile;

    @Bean
    public ProfileProperties profileDependentProperties() {
        return new ProfileProperties(dbfile);
    }

    @AllArgsConstructor
    @Getter
    public static class ProfileProperties {
        private String dbfile;
    }
}
