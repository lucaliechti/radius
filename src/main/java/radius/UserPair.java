package radius;

import com.google.auto.value.AutoValue;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

@AutoValue
public abstract class UserPair {

	public abstract User user1();

	public abstract User user2();

	public Set<String> commonLanguages() {
		Set<String> commonLanguages = new HashSet<>(user1().getLanguages());
		commonLanguages.retainAll(user2().getLanguages());
		return commonLanguages;
	}

	public Set<Integer> commonLocations() {
		Set<Integer> commonLocations = new HashSet<Integer>(user1().getLocations());
		commonLocations.retainAll(user2().getLocations());
		return commonLocations;
	}

	public double disagreementScore() {
		List<User.TernaryAnswer> answers1 = user1().getRegularanswers();
		List<User.TernaryAnswer> answers2 = user2().getRegularanswers();

		double disagreementScore = 0.0;
		final double DISAGREEMENT_WEIGHT = 1.0;
		final double AGREEMENT_WEIGHT = 0.1;

		for (int i = 0; i < answers1.size(); i++) {
			if (answers1.get(i) == User.TernaryAnswer.TRUE && answers2.get(i) == User.TernaryAnswer.FALSE ||
			    answers1.get(i) == User.TernaryAnswer.FALSE && answers2.get(i) == User.TernaryAnswer.TRUE) {
				disagreementScore += DISAGREEMENT_WEIGHT;
			}
			else if (answers1.get(i) == answers2.get(i) && !(answers1.get(i) == User.TernaryAnswer.DONTCARE)) {
				disagreementScore -= AGREEMENT_WEIGHT;
			}
		}

		return disagreementScore;
	}

	public double waitingTime(Instant now) {
		return (ChronoUnit.SECONDS.between(user1().getDateModified().toInstant(), now)
				+ ChronoUnit.SECONDS.between(user2().getDateModified().toInstant(), now))
			/ (1.0 * ChronoUnit.DAYS.getDuration().getSeconds());
	}

	public boolean compatible(double minimalDisagreementScore) {
		if (commonLanguages().isEmpty() || commonLocations().isEmpty()) {
			return false;
		}
		return !(disagreementScore() < minimalDisagreementScore);
	}

	public static UserPair of(User user1, User user2) {
		checkNotNull(user1);
		checkNotNull(user2);

		return new AutoValue_UserPair(user1, user2);
	}
}
