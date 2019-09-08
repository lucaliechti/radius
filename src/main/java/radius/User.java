package radius;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class User {
	private long id;
	private String firstname;
	private String lastname;
	@Email(message="{error.Email}")
	private String email;
	@Size(min=8, message="{error.sizePW}")
	private String password;
	private String canton;
	private userStatus status;
	private userModus modus;
	private boolean enabled; 			//if email has been confirmed
	private boolean answeredRegular; 	//if *regular* questions have been answered
	private boolean banned;
	private String motivation;
	private List<Integer> locations;
	private List<String> languages;
	private List<answer> regularanswers;
	private List<answer> specialanswers;
	private String uuid;
	private Timestamp dateModified;

	public User(
			String _firstname,
			String _lastname,
			String _email,
			String _password,
			String _canton,
			userModus _modus,
			userStatus _status,
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
		this.modus = _modus;
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

	//for first time registration
	public User(String _firstName, String _lastName, String _canton, String _email, String _password) {
		this.firstname = _firstName;
		this.lastname = _lastName;
		this.canton = _canton;
		this.email = _email;
		this.password = _password;
		this.uuid = UUID.randomUUID().toString();
	}

	public User() { }
	
	public long getId() {
		return id;
	}
	public String getFirstname() {
		return firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public String getEmail() {
		return email;
	}
	public String getPassword() {
		return password;
	}
	public String getCanton() {
		return canton;
	}
	public userStatus getStatus() {
		return status;
	}
	public Boolean getEnabled() {
		return enabled;
	}
	public Boolean getAnswered() {
		return answeredRegular;
	}
	public List<answer> getRegularanswers() {
		return regularanswers;
	}
	public List<String> getRegularAnswersAsListOfStrings() {
		return regularanswers.stream().map(User::convertAnswerToString).collect(Collectors.toList());
	}
	public List<answer> getSpecialanswers() {
		return specialanswers;
	}
	public List<String> getSpecialAnswersAsListOfStrings() {
		return specialanswers.stream().map(User::convertAnswerToString).collect(Collectors.toList());
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setCanton(String canton) {
		this.canton = canton;
	}
	public void setStatus(String _status) {
		this.status = convertStatus(_status);
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public void setAnsweredRegular(Boolean answered) {
		this.answeredRegular = answered;
	}
	public void setBanned(Boolean banned) {
		this.banned = banned;
	}
	public boolean isBanned() {
		return banned;
	}
	public Timestamp getDateModified() {
		return dateModified;
	}
	public void setRegularanswers(List<String> answers) {
		regularanswers = answers.stream().map(User::convertAnswer).collect(Collectors.toList());
	}
	public void setSpecialanswers(List<String> _specialanswers) {
		specialanswers = _specialanswers.stream().map(User::convertAnswer).collect(Collectors.toList());
	}
	public List<Integer> getLocations() {
		return locations;
	}
	public void setLocations(List<Integer> locations) {
		this.locations = locations;
	}
	public List<String> getLanguages() {
		return languages;
	}
	public void setLanguages(List<String> list) {
		this.languages = list;
	}
	public userModus getModus() {
		return modus;
	}
	public String getModusAsString() {
		return convertModusToString(this.modus);
	}
	public void setModus(String _modus) {
		this.modus = convertModus(_modus);
	}
	public String getMotivation() {
		return motivation;
	}
	public void setMotivation(String motivation) {
		this.motivation = motivation;
	}
	public String getUuid() { return uuid; }
	public void setUuid(String _uuid) { this.uuid = _uuid; }

	public static userStatus convertStatus(String status) {
		switch (status) {
			case "WAITING":
				return userStatus.WAITING;	
			case "MATCHED":
				return userStatus.MATCHED;
			case "INACTIVE":
				return userStatus.INACTIVE;
			default:
				return null;
		}
	}
	
	public static String convertStatusToString(userStatus _status) {
		switch (_status) {
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
	
	public static userModus convertModus(String modus) {
		switch (modus) {
			case "SINGLE":
				return userModus.SINGLE;
			case "PAIR":
				return userModus.PAIR;
			case "EITHER":
				return userModus.EITHER;
			default:
				return null;
		}
	}
	
	public static String convertModusToString(userModus _modus) {
		switch (_modus) {
			case SINGLE:
				return "SINGLE";
			case PAIR:
				return "PAIR";
			case EITHER:
				return "EITHER";
			default:
				return null;
		}
	}

	public static answer convertAnswer(String _answer) {
		switch (_answer) {
			case "TRUE":
				return answer.TRUE;
			case "FALSE":
				return answer.FALSE;
			case "DONTCARE":
				return answer.DONTCARE;
			default:
				return null;
		}
	}

	public static String convertAnswerToString(answer _answer) {
		if(_answer == null) { System.out.println("hoooi NULL"); return "DONTCARE"; }
		switch (_answer) {
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

	public enum userStatus {
		WAITING,
		MATCHED,
		INACTIVE
	}
	
	public enum userModus {
		SINGLE,
		PAIR,
		EITHER
	}

	public enum answer {
		TRUE,
		FALSE,
		DONTCARE
	}
}
