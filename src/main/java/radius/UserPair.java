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

	public static Optional<User.userModus> commonModus(User.userModus modus1, User.userModus modus2) {
		Set<User.userModus> available = new HashSet<>(availableModi(modus1));
		available.retainAll(availableModi(modus2));

		if (available.size() == 2) {
			return Optional.of(User.userModus.EITHER);
		}
		else if (available.size() == 1) {
			return Optional.of(available.iterator().next());
		}
		else {
			return Optional.empty();
		}
	}

	boolean compatibleModi() {
		return commonModus(user1().getModus(), user2().getModus()).isPresent();
	}

	private static Set<User.userModus> availableModi(User.userModus modus) {
		if (modus == User.userModus.EITHER) {
			return ImmutableSet.of(User.userModus.SINGLE, User.userModus.PAIR);
		}
		else {
			return ImmutableSet.of(modus);
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
		checkNotNull(user1);
		checkNotNull(user2);

		return new AutoValue_UserPair(user1, user2);
	}
}
