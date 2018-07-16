package radius;

import java.util.ArrayList;

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
	private Boolean enabled; 	//if email has been confirmed
	private Boolean answered; 	//if questions have been answered
	private ArrayList<Integer> locations;
	private ArrayList<String> languages;
	private ArrayList<Boolean> questions;
	
	public User(String _firstname, String _lastname, String _email, String _canton, userStatus status, Boolean _enabled, Boolean _answered, ArrayList<Integer> _locations, ArrayList<String> _languages, ArrayList<Boolean> _questions) {
		this.firstname = _firstname;
		this.lastname = _lastname;
		this.email = _email;
		this.canton = _canton;
		this.status = status;
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
	public ArrayList<Boolean> getQuestions() {
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
	public void setStatus(userStatus status) {
		this.status = status;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public void setAnswered(Boolean answered) {
		this.answered = answered;
	}
	public void setQ1(ArrayList<Boolean> questions) throws Exception {
		if(questions.size() != 5) {
			throw new Exception("An array of != 5 Boolean values has been passed.");
		}
		this.questions = questions;
	}
	public ArrayList<Integer> getLocations() {
		return locations;
	}
	public void setLocations(ArrayList<Integer> locations) {
		this.locations = locations;
	}
	public ArrayList<String> getLanguages() {
		return languages;
	}
	public void setLanguages(ArrayList<String> languages) {
		this.languages = languages;
	}

	//new: only username and password provided
	//waiting: email confirmed and questions answered
	//matched: match found, waiting to radius
	//inactive: user checked the "inactive" flag
	public enum userStatus {
		NEW,
		WAITING,
		MATCHED,
		INACTIVE
	}
}
