package radius;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.Optional;

@Getter
@AllArgsConstructor
public class HalfEdge {

	private final String email1;
	private final String email2;
	private final Boolean active;
	private final Optional<Boolean> meetingConfirmed;
	private final Timestamp dateCreated;
	private final Optional<Timestamp> dateInactive;
	private final String mode;

}
