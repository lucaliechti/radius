package radius;

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import radius.web.controller.MatchingController;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static radius.User.userModus.EITHER;
import static radius.User.userModus.PAIR;
import static radius.User.userModus.SINGLE;

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
				ImmutableList.of("FALSE", "TRUE", "DONTCARE", "DONTCARE", "DONTCARE"),
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
				ImmutableList.of("TRUE", "FALSE", "DONTCARE", "DONTCARE", "DONTCARE"),
				Timestamp.from(Instant.ofEpochSecond(0))
		);
		UserPair userPair = UserPair.of(user1, user2);

		assertEquals(20, userPair.numberOfDisagreements());
		//assertFalse(MatchingController.Edge.optFromUserPair(userPair, Instant.ofEpochSecond(0)).isPresent());
	}

	@Test
	public void commonModus() {
		assertEquals(Optional.of(SINGLE), UserPair.commonModus(SINGLE, SINGLE));
		assertEquals(Optional.empty(), UserPair.commonModus(SINGLE, PAIR));
		assertEquals(Optional.of(SINGLE), UserPair.commonModus(SINGLE, EITHER));
		assertEquals(Optional.empty(), UserPair.commonModus(PAIR, SINGLE));
		assertEquals(Optional.of(PAIR), UserPair.commonModus(PAIR, PAIR));
		assertEquals(Optional.of(PAIR), UserPair.commonModus(PAIR, EITHER));
		assertEquals(Optional.of(SINGLE), UserPair.commonModus(EITHER, SINGLE));
		assertEquals(Optional.of(PAIR), UserPair.commonModus(EITHER, PAIR));
		assertEquals(Optional.of(EITHER), UserPair.commonModus(EITHER, EITHER));
	}
}