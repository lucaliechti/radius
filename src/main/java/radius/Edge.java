package radius;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.Optional;

@Getter
@AllArgsConstructor
public class Edge {
    private String start;
    private String end;
    private double weight;

    public static Optional<Edge> optionalFromUserPair(UserPair userPair, Instant now, boolean waitingTime, double min) {
        if (userPair.compatible(min)) {
            return Optional.of(new Edge(userPair.user1().getEmail(), userPair.user2().getEmail(),
                    userPair.disagreementScore() + (waitingTime ? (1-(1/userPair.waitingTime(now))) : 0.0 )));
        } else {
            return Optional.empty();
        }
    }

}
