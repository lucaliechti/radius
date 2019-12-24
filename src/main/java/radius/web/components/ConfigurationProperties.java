package radius.web.components;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


@Getter
@Component
@PropertySource("classpath:config/config.properties")
public class ConfigurationProperties {

    @Value("${matching.minimum.disagreement.score}")
    private double minimumScore;

    @Value("${matching.factor.waitingtime}")
    private boolean waitingTime;

    @Value("${matching.active}")
    private boolean active;

}
