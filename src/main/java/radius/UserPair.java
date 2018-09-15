package radius;

import com.google.auto.value.AutoValue;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Immutable class containing a pair of users which could potentially be matched.
 */
@AutoValue
public abstract class UserPair {
	private static final int MINIMAL_NUMBER_OF_DISAGREEMENTS = 3;

	public abstract User user1();

	public abstract User user2();

	Set<String> commonLanguages() {
		Set<String> commonLanguages = new HashSet<>(user1().getLanguages());
		commonLanguages.retainAll(user2().getLanguages());
		return commonLanguages;
	}

	Set<Integer> commonLocations() {
		Set<Integer> commonLocations = new HashSet<Integer>(user1().getLocations());
		commonLocations.retainAll(user2().getLocations());
		return commonLocations;
	}

	public int numberOfDisagreements() {
		List<Boolean> answers1 = user1().getQuestions();
		List<Boolean> answers2 = user2().getQuestions();

		int nDisagreements = 0;

		for (int i = 0; i < answers1.size(); i++) {
			if (answers1.get(i) != answers2.get(i)) {
				nDisagreements += 1;
			}
		}

		return nDisagreements;
	}

	boolean compatibleModi() {
		if (user1().getModus() == null || user2().getModus() == null) {
			return false;
		} else if (user1().getModus() == User.userModus.EITHER || user2().getModus() == User.userModus.EITHER) {
			return true;
		} else {
			return (user1().getModus() == user2().getModus());
		}
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

		if (!compatibleModi()) {
			return false;
		}

		if (numberOfDisagreements() < MINIMAL_NUMBER_OF_DISAGREEMENTS) {
			return false;
		}

		return true;
	}

	public static UserPair of(User user1, User user2) {
		return new AutoValue_UserPair(user1, user2);
	}
}
