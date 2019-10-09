package radius.web.components;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import java.util.Objects;

@Configuration
@PropertySource("classpath:config/realworld.properties")
public class RealWorldConfiguration {

    @Autowired
    Environment env;

    @Bean
    public RealWorldProperties realWorld() {
        boolean special = Boolean.parseBoolean(env.getProperty("specialIsActive"));
        int votes = Integer.parseInt(Objects.requireNonNull(env.getProperty("numberOfVotes")));
        int regular = Integer.parseInt(Objects.requireNonNull(env.getProperty("numberOfRegularQuestions")));
        String current = env.getProperty("currentVote");

        return new RealWorldProperties(special, votes, regular, current);
    }

    @AllArgsConstructor
    @Getter
    public static class RealWorldProperties {
        private boolean specialIsActive;
        private int numberOfVotes;
        private int numberOfRegularQuestions;
        private String currentVote;
    }
}
