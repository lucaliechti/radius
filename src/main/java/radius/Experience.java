package radius;

import java.util.Date;

//import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
//import org.hibernate.validator.constraints.NotEmpty;

//import org.apache.commons.lang.builder.EqualsBuilder;

//@SuppressWarnings("deprecation")
public class Experience {
	
	private long id;
	@NotBlank(message="{error.notBlank}")
	private String experience;
	private Date datecreate;
	private Date datemodify;
	
//	@Email
//	@NotBlank
	private String userEmail;
	
	//whole PLACE logic discarded for the moment!
	
	//constructor without time; for creating experiences
	public Experience(String _exp, String _user_email) {
		this.experience = _exp;
		this.userEmail = _user_email;
	}
	
	//constructor with time; for retrieving experiences from database
	public Experience(long _id, String _exp, Date _datecreate, Date _datemodify, String _user_email) {
		this.id = _id;
		this.experience = _exp;
		this.datecreate = _datecreate;
		this.datemodify = _datemodify;
		this.userEmail = _user_email;
	}
	//noarg constructor
	public Experience() {
	}
	
	public long getId() {
		return id;
	}
	public String getExperience(){
		return experience;
	}
	public String getUseremail() {
		return userEmail;
	}
	public Date getDatecreate() {
		return datecreate;
	}
	public Date getDatemodify() {
		return datemodify;
	}
	
	public void setExperience(String exp) {
		this.experience = exp;
	}
	public void setUseremail(String userEmail) {
		this.userEmail = userEmail;
	}
	public void setDatemodify(Date _mod) {
		this.datemodify = _mod;
	}
	
//	@Override
//	public boolean equals(Object that) {
//		String[] exclude = {"experience", "time", "user", "place"};
//		return EqualsBuilder.reflectionEquals(this, that, exclude);
//	}
}
