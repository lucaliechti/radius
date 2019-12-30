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

	public double disagreementScore(MatchingMode mode, boolean factorWaitingTime, Instant now) {
		List<User.TernaryAnswer> answers1 = mode.equals(MatchingMode.REGULAR) ? user1().getRegularanswers() :
				user1().getSpecialanswers();
		List<User.TernaryAnswer> answers2 = mode.equals(MatchingMode.REGULAR) ? user2().getRegularanswers() :
				user2().getSpecialanswers();

		if(answers1 == null || answers2 == null
				|| answers1.isEmpty() || answers2.isEmpty()
				|| answers1.size() != answers2.size()) {
			return 0.0;
		}

		double disagreementScore = 0.0;
		final double DISAGREEMENT_WEIGHT = 1.0;
		final double AGREEMENT_WEIGHT = 0.1;
		for (int i = 0; i < answers1.size(); i++) {
			if (answersDisagree(answers1.get(i), answers2.get(i))) {
				disagreementScore += DISAGREEMENT_WEIGHT;
			}
			else if (answersAgree(answers1.get(i), answers2.get(i))) {
				disagreementScore -= AGREEMENT_WEIGHT;
			}
		}
		return disagreementScore + (factorWaitingTime ? weighted(waitingTime(now)) : 0.0);
	}

	private double weighted(double waitingTime) {
		return (Math.atan(waitingTime/3 - 3) + Math.atan(3)) / (Math.PI/2 + Math.atan(3));
	}

	private double waitingTime(Instant now) {
		return (ChronoUnit.SECONDS.between(user1().getDateModified().toInstant(), now)
				+ ChronoUnit.SECONDS.between(user2().getDateModified().toInstant(), now))
			/ (1.0 * ChronoUnit.DAYS.getDuration().getSeconds());
	}

	public boolean compatible(MatchingMode mode, int minDisagreementsRegular, int minDisagreementsSpecial) {
		if (commonLanguages().isEmpty() || commonLocations().isEmpty() || bothPrivate()) {
			return false;
		}
		return !(disagreements(mode) < (mode.equals(MatchingMode.SPECIAL) ? minDisagreementsSpecial :
				minDisagreementsRegular));
	}

	public int disagreements(MatchingMode mode) {
		List<User.TernaryAnswer> answers1 = mode.equals(MatchingMode.REGULAR) ? user1().getRegularanswers() :
				user1().getSpecialanswers();
		List<User.TernaryAnswer> answers2 = mode.equals(MatchingMode.REGULAR) ? user2().getRegularanswers() :
				user2().getSpecialanswers();

		if(answers1 == null || answers2 == null
				|| answers1.isEmpty() || answers2.isEmpty()
				|| answers1.size() != answers2.size()) {
			return 0;
		}

		int disagreements = 0;
		for (int i = 0; i < answers1.size(); i++) {
			if (answersDisagree(answers1.get(i), answers2.get(i))) {
				disagreements++;
			}
		}
		return disagreements;
	}

	private boolean answersDisagree(User.TernaryAnswer answer1, User.TernaryAnswer answer2) {
		return !answer1.equals(User.TernaryAnswer.DONTCARE) && !answer2.equals(User.TernaryAnswer.DONTCARE)
				&& !answer1.equals(answer2);
	}

	private boolean answersAgree(User.TernaryAnswer answer1, User.TernaryAnswer answer2) {
		return answer1.equals(answer2) && !answer1.equals(User.TernaryAnswer.DONTCARE);
	}

	private boolean bothPrivate() {
		return user1().isPrivateUser() && user2().isPrivateUser();
	}

	public static UserPair of(User user1, User user2) {
		checkNotNull(user1);
		checkNotNull(user2);

		return new AutoValue_UserPair(user1, user2);
	}
}
