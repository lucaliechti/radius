package reach;

public class User {
	private long id;
	private String username;
	private String firstname;
	private String lastname;
	private String email;
	private String password;
	private String location;
	private userStatus status;
	private Boolean enabled; 	//if email has been confirmed
	private Boolean answered; 	//if questions have been answered
	private Boolean q1;
	private Boolean q2;
	private Boolean q3;
	private Boolean q4;
	private Boolean q5;
	
	public User(String _username, String _firstname, String _lastname, String _email, String _location, userStatus string, Boolean _enabled, Boolean _answered, Boolean _q1, Boolean _q2, Boolean _q3, Boolean _q4, Boolean _q5) {
		this.username = _username;
		this.firstname = _firstname;
		this.lastname = _lastname;
		this.email = _email;
		this.location = _location;
		this.status = string;
		this.enabled = _enabled;
		this.answered = _answered;
		this.q1 = _q1;
		this.q2 = _q2;
		this.q3 = _q3;
		this.q4 = _q4;
		this.q5 = _q5;
	}
	public User(String _username, String _password) {
		this.username = _username;
		this.password = _password;
	}
	public User() {
	}
	
	public long getId() {
		return id;
	}
	public String getUsername() {
		return username;
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
	public Boolean getQ1() {
		return q1;
	}
	public Boolean getQ2() {
		return q2;
	}
	public Boolean getQ3() {
		return q3;
	}
	public Boolean getQ4() {
		return q4;
	}
	public Boolean getQ5() {
		return q5;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setUsername(String username) {
		this.username = username;
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
	public void setQ1(Boolean q1) {
		this.q1 = q1;
	}
	public void setQ2(Boolean q2) {
		this.q2 = q2;
	}
	public void setQ3(Boolean q3) {
		this.q3 = q3;
	}
	public void setQ4(Boolean q4) {
		this.q4 = q4;
	}
	public void setQ5(Boolean q5) {
		this.q5 = q5;
	}
	
	//new: only username and password provided
	//waiting: email confirmed and questions answered
	//matched: match found, waiting to reach
	//inactive: user checked the "inactive" flag
	public enum userStatus {
		NEW,
		WAITING,
		MATCHED,
		INACTIVE
	}
}
