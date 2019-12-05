package radius.web.components;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Component
@PropertySource("classpath:config/realworld.properties")
public class RealWorldProperties {

    @Value("${specialIsActive}")
    boolean specialIsActive;

    @Value("${numberOfVotes}")
    int numberOfVotes;

    @Value("${numberOfRegularQuestions}")
    int numberOfRegularQuestions;

    @Value("${currentVote}")
    String currentVote;

}
