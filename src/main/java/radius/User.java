package radius;

import java.util.ArrayList;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

public class User {
	private long id;
	private String firstname;
	private String lastname;
	@Email(message="{error.Email}")
	private String email;
	@Size(min=8, message="{error.sizePW}")
	private String password;
	private String location;
	private userStatus status;
	private Boolean enabled; 	//if email has been confirmed
	private Boolean answered; 	//if questions have been answered
	private ArrayList<Boolean> questions;
	
	public User(String _firstname, String _lastname, String _email, String _location, userStatus string, Boolean _enabled, Boolean _answered, ArrayList<Boolean> _questions) {
		this.firstname = _firstname;
		this.lastname = _lastname;
		this.email = _email;
		this.location = _location;
		this.status = string;
		this.enabled = _enabled;
		this.answered = _answered;
		this.questions = _questions;
	}
	public User(String _email, String _password) {
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
	public String getLocation() {
		return location;
	}
//	public String getLanguage() {
//		return language;
//	}
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
	public void setLocation(String location) {
		this.location = location;
	}
//	public void setLanguage(String language) {
//		this.language = language;
//	}
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
