package radius;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Size;

//import org.hibernate.validator.constraints.Email;
import javax.validation.constraints.Email;

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
	private boolean enabled; 	//if email has been confirmed
	private boolean answered; 	//if questions have been answered
	private String motivation;
	private List<Integer> locations;
	private List<String> languages;
	private List<Boolean> questions;
	
	
	public User(String _firstname, String _lastname, String _email, String _password, String _canton, userModus _modus, userStatus _status, String _motivation, boolean _enabled, boolean _answered, List<Integer> _locations, ArrayList<String> _languages, List<Boolean> _questions) {
		this.firstname = _firstname;
		this.lastname = _lastname;
		this.email = _email;
		this.password = _password;
		this.canton = _canton;
		this.modus = _modus;
		this.status = _status;
		this.motivation = _motivation;
		this.enabled = _enabled;
		this.answered = _answered;
		this.locations = _locations;
		this.languages = _languages;
		this.questions = _questions;
	}
	
	//for first time registration
	public User(String _firstName, String _lastName, String _canton, String _email, String _password) {
		this.firstname = _firstName;
		this.lastname = _lastName;
		this.canton = _canton;
		this.email = _email;
		this.password = _password;
	}
	public User() {
	}
	
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
		return answered;
	}
	public List<Boolean> getQuestions() {
		return questions;
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
	public void setAnswered(Boolean answered) {
		this.answered = answered;
	}
	public void setQuestions(List<Boolean> questions) throws Exception {
		if(questions.size() != 5) {
			throw new Exception("An array of != 5 Boolean values has been passed.");
		}
		this.questions = questions;
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
	public void setModus(String _modus) {
		this.modus = convertModus(_modus.toUpperCase());
		System.out.println(this.modus);
	}

	public String getMotivation() {
		return motivation;
	}
	public void setMotivation(String motivation) {
		this.motivation = motivation;
	}

	public static userStatus convertStatus(String status) {
		if(status == null) {
			return null;
		}
		switch (status) {
			case "NEW":
				return userStatus.NEW;
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
		case NEW:
			return "NEW";
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
		if(modus == null) {
			return null;
		}
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
	
	public enum userStatus {
		NEW,
		WAITING,
		MATCHED,
		INACTIVE
	}
	
	public enum userModus {
		SINGLE,
		PAIR,
		EITHER
	}
}
