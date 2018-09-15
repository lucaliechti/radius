package radius;

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import radius.web.controller.MatchingController;

import java.sql.Timestamp;
import java.time.Instant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class UserPairTest {
	@Test
	public void numberOfDisagreementsFilterTest() {
		User user1 = new User(
				"Firstname", "Lastname", "email",
				"password",
				"BE",
				User.userModus.SINGLE,
				User.userStatus.WAITING,
				"",
				true,
				true,
				false,
				ImmutableList.of(1, 2, 3),
				ImmutableList.of("DE", "FR"),
				ImmutableList.of(true, true, true, true, true),
				Timestamp.from(Instant.ofEpochSecond(0))
		);
		User user2 = new User(
				"Firstname", "Lastname", "email",
				"password",
				"BE",
				User.userModus.SINGLE,
				User.userStatus.WAITING,
				"",
				true,
				true,
				false,
				ImmutableList.of(1, 2, 3),
				ImmutableList.of("DE", "FR"),
				ImmutableList.of(true, true, true, true, false),
				Timestamp.from(Instant.ofEpochSecond(0))
		);
		UserPair userPair = UserPair.of(user1, user2);

		assertEquals(1, userPair.numberOfDisagreements());
		assertFalse(MatchingController.Edge.optFromUserPair(userPair, Instant.ofEpochSecond(0)).isPresent());
	}
}
