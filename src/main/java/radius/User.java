package radius;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
public class User {
	private long id;
	private String firstname;
	private String lastname;
	private String email;
	private String password;
	private String canton;
	private UserStatus status;
	private boolean enabled;
	private boolean answeredRegular;
	private boolean banned;
	private String motivation;
	private List<Integer> locations;
	private List<String> languages;
	private List<TernaryAnswer> regularanswers;
	private List<TernaryAnswer> specialanswers;
	private String uuid;
	private Timestamp dateModified;

	public User(String _firstName, String _lastName, String _canton, String _email, String _password) {
		this.firstname = _firstName;
		this.lastname = _lastName;
		this.canton = _canton;
		this.email = _email;
		this.password = _password;
		this.uuid = UUID.randomUUID().toString();
	}

	public User(
			String _firstname,
			String _lastname,
			String _email,
			String _password,
			String _canton,
			UserStatus _status,
			String _motivation,
			boolean _enabled,
			boolean _answeredRegular,
			boolean _banned,
			List<Integer> _locations,
			List<String> _languages,
			List<String> _regularanswers,
//			List<String> _specialanswers,
			Timestamp _dateModified
	) {
		this.firstname = _firstname;
		this.lastname = _lastname;
		this.email = _email;
		this.password = _password;
		this.canton = _canton;
		this.status = _status;
		this.motivation = _motivation;
		this.enabled = _enabled;
		this.answeredRegular = _answeredRegular;
		this.banned = _banned;
		this.locations = _locations;
		this.languages = _languages;
		this.regularanswers = _regularanswers.stream().map(User::convertAnswer).collect(Collectors.toList());
//		this.specialanswers = _specialanswers.stream().map(User::convertAnswer).collect(Collectors.toList());
		this.dateModified = _dateModified;
	}



	public User() { }
	
	public List<String> getRegularAnswersAsListOfStrings() {
		return regularanswers.stream().map(User::convertAnswerToString).collect(Collectors.toList());
	}
	public List<String> getSpecialAnswersAsListOfStrings() {
		return specialanswers.stream().map(User::convertAnswerToString).collect(Collectors.toList());
	}
	public void setRegularanswers(List<String> answers) {
		regularanswers = answers.stream().map(User::convertAnswer).collect(Collectors.toList());
	}
	public void setSpecialanswers(List<String> _specialanswers) {
		specialanswers = _specialanswers.stream().map(User::convertAnswer).collect(Collectors.toList());
	}

	public static UserStatus convertStatus(String status) {
		switch (status) {
			case "WAITING":
				return UserStatus.WAITING;
			case "MATCHED":
				return UserStatus.MATCHED;
			case "INACTIVE":
				return UserStatus.INACTIVE;
			default:
				return null;
		}
	}
	
	public static String convertStatusToString(UserStatus status) {
		switch (status) {
			case WAITING:
				return "WAITING";
			case MATCHED:
				return "MATCHED";
			case INACTIVE:
				return "INACTIVE";
			default:
				return null;
		}
	}

	public static TernaryAnswer convertAnswer(String answer) {
		switch (answer) {
			case "TRUE":
				return TernaryAnswer.TRUE;
			case "FALSE":
				return TernaryAnswer.FALSE;
			case "DONTCARE":
				return TernaryAnswer.DONTCARE;
			default:
				return null;
		}
	}

	public static String convertAnswerToString(TernaryAnswer answer) {
		if (answer == null) {
			return null;
		}
		switch (answer) {
			case TRUE:
				return "TRUE";
			case FALSE:
				return "FALSE";
			case DONTCARE:
				return "DONTCARE";
			default:
				return null;
		}
	}
	
	public static String createLocString(List<Integer> locs){
		return locs.stream().map(String::valueOf).collect(Collectors.joining(";"));
	}

	public enum UserStatus {
		WAITING,
		MATCHED,
		INACTIVE
	}

	public enum TernaryAnswer {
		TRUE,
		FALSE,
		DONTCARE
	}
}
