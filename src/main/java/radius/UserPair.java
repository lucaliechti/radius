package radius;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Immutable class containing a pair of users which could potentially be matched.
 */
@AutoValue
public abstract class UserPair {
	private static final int MINIMAL_NUMBER_OF_DISAGREEMENTS = 3;

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

	//TODO: Change name of function, it is now misleading
	public int numberOfDisagreements() {
		List<User.TernaryAnswer> answers1 = user1().getRegularanswers();
		List<User.TernaryAnswer> answers2 = user2().getRegularanswers();

		int matchScore = 0;
		final int DISAGREEMENT_WEIGHT = 10; //using ints for now so nothing changes downstream
		final int AGREEMENT_WEIGHT = 3;

		for (int i = 0; i < answers1.size(); i++) {
			if (answers1.get(i) == User.TernaryAnswer.TRUE && answers2.get(i) == User.TernaryAnswer.FALSE ||
			    answers1.get(i) == User.TernaryAnswer.FALSE && answers2.get(i) == User.TernaryAnswer.TRUE) {
				matchScore += DISAGREEMENT_WEIGHT;
			}
			else if (answers1.get(i) == answers2.get(i) && !(answers1.get(i) == User.TernaryAnswer.DONTCARE)) {
				matchScore -= AGREEMENT_WEIGHT;
			}
		}

		return matchScore;
	}

	public double totalDaysUnmodified(Instant now) {
		return (ChronoUnit.SECONDS.between(user1().getDateModified().toInstant(), now) + ChronoUnit.SECONDS.between(user2().getDateModified().toInstant(), now)) / (1.0 * ChronoUnit.DAYS.getDuration().getSeconds());
	}

	public boolean compatible() {
		if (commonLanguages().isEmpty()) {
			// no common languages
			return false;
		}

		if (commonLocations().isEmpty()) {
			// no common locations
			return false;
		}

		if (numberOfDisagreements() < MINIMAL_NUMBER_OF_DISAGREEMENTS) {
			return false;
		}

		return true;
	}

	public static UserPair of(User user1, User user2) {
		checkNotNull(user1);
		checkNotNull(user2);

		return new AutoValue_UserPair(user1, user2);
	}
}
