package radius;

import com.google.auto.value.AutoValue;
import com.google.common.base.Preconditions;

import java.sql.Timestamp;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

@AutoValue
public abstract class HalfEdge {
	public abstract String email1();
	public abstract String email2();
	public abstract Boolean active();
	public abstract Boolean meetingConfirmed();
	public abstract Timestamp dateCreated();
	public abstract Optional<Timestamp> dateInactive();

	public static HalfEdge of(String email1, String email2, boolean active, boolean meetingConfirmed, Timestamp dateCreated, Optional<Timestamp> dateInactive) {
		checkNotNull(email1);
		checkNotNull(email2);
		checkNotNull(dateCreated);
		dateInactive.ifPresent(Preconditions::checkNotNull);

		return new AutoValue_HalfEdge(email1, email2, active, meetingConfirmed, dateCreated, dateInactive);
	}
}
