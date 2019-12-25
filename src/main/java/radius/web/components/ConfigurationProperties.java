package radius.web.components;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


@Getter
@Component
@PropertySource("classpath:config/config.properties")
public class ConfigurationProperties {

    @Value("${matching.factor.waitingtime}")
    private boolean waitingTime;

    @Value("${matching.active}")
    private boolean active;

    @Value("${matching.minimum.disagreements.regular}")
    private int minDisagreementsRegular;

    @Value("${matching.minimum.disagreements.special}")
    private int minDisagreementsSpecial;

}
