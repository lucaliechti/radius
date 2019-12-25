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

    public static Optional<Edge> optionalFromUserPair(UserPair userPair, Instant now, boolean waitingTime,
                                                      int minDisagreementsRegular, int minDisagreementsSpecial,
                                                      MatchingMode mode) {
        if (userPair.compatible(mode, minDisagreementsRegular, minDisagreementsSpecial)) {
            return Optional.of(
                new Edge(
                    userPair.user1().getEmail(),
                    userPair.user2().getEmail(),
                    userPair.disagreementScore(mode, waitingTime, now)
                )
            );
        } else {
            return Optional.empty();
        }
    }
}
