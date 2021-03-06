package radius;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;
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
	private boolean banned;
	private String motivation;
	private List<Integer> locations;
	private List<String> languages;
	private List<TernaryAnswer> regularanswers;
	private List<TernaryAnswer> specialanswers;
	private boolean privateUser;
	private String uuid;
	private Timestamp dateCreated;
	private Timestamp dateModified;
	private Timestamp lastLogin;

	public User(String firstName, String lastName, String canton, String email, String password) {
		this.firstname = firstName;
		this.lastname = lastName;
		this.canton = canton;
		this.email = email;
		this.password = password;
		this.uuid = UUID.randomUUID().toString();
	}

	public User(
		String firstname,
		String lastname,
		String email,
		String password,
		String canton,
		UserStatus status,
		String motivation,
		boolean enabled,
		boolean banned,
		List<Integer> locations,
		List<String> languages,
		List<String> regularanswers,
		List<String> specialanswers,
		boolean privateUser,
		Timestamp dateCreated,
		Timestamp dateModified,
		Timestamp lastLogin,
		String uuid
	) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.password = password;
		this.canton = canton;
		this.status = status;
		this.motivation = motivation;
		this.enabled = enabled;
		this.banned = banned;
		this.locations = locations;
		this.languages = languages;
		this.regularanswers = regularanswers.stream().map(User::convertAnswer).collect(Collectors.toList());
		this.specialanswers = specialanswers.stream().map(User::convertAnswer).collect(Collectors.toList());
		this.privateUser = privateUser;
		this.dateCreated = dateCreated;
		this.dateModified = dateModified;
		this.lastLogin = lastLogin;
		this.uuid = uuid;
	}

	public void setRegularanswers(List<String> answers) {
		regularanswers = answers.stream().map(User::convertAnswer).collect(Collectors.toList());
	}
	public void setSpecialanswers(List<String> _specialanswers) {
		specialanswers = _specialanswers.stream().map(User::convertAnswer).collect(Collectors.toList());
	}
	public Locale preferredLocale() {
		return new Locale(languages.contains("DE") ? "de" : (languages.contains("FR") ? "fr" : "en"));
	}
	public int answeredRegularQuestions() {
		return (int) regularanswers.stream().filter(answer -> !answer.equals(TernaryAnswer.DONTCARE)).count();
	}
	public int answeredSpecialQuestions() {
		return (int) specialanswers.stream().filter(answer -> !answer.equals(TernaryAnswer.DONTCARE)).count();
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
	
	public String locationString(){
		return locations.stream().map(String::valueOf).collect(Collectors.joining(";"));
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
