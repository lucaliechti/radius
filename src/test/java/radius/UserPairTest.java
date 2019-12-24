package radius;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class UserPairTest {
	@Test
	public void numberOfDisagreementsFilterTest() {
		User user1 = new User(
				"Firstname", "Lastname", "email",
				"password",
				"BE",
				User.UserStatus.WAITING,
				"",
				true,
				false,
				ImmutableList.of(1, 2, 3),
				ImmutableList.of("DE", "FR"),
				ImmutableList.of("FALSE", "TRUE", "DONTCARE", "DONTCARE", "DONTCARE"),
				Collections.emptyList(),
				Timestamp.from(Instant.ofEpochSecond(0)),
				"d42f2b69-f065-457d-9a57-069b2e3b1da1"
		);
		User user2 = new User(
				"Firstname", "Lastname", "email",
				"password",
				"BE",
				User.UserStatus.WAITING,
				"",
				true,
				false,
				ImmutableList.of(1, 2, 3),
				ImmutableList.of("DE", "FR"),
				ImmutableList.of("TRUE", "FALSE", "DONTCARE", "DONTCARE", "DONTCARE"),
				Collections.emptyList(),
				Timestamp.from(Instant.ofEpochSecond(0)),
				"4d23cebd-2d6b-46b4-87eb-24d76fbe13fc"
		);
		UserPair userPair = UserPair.of(user1, user2);

		assertEquals(20, userPair.disagreementScore());
		//assertFalse(MatchingController.Edge.optFromUserPair(userPair, Instant.ofEpochSecond(0)).isPresent());
	}
}
