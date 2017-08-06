package reach;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;

public class Experience {
	private Long exp_id;
	private String experience;
	private Date time;
	private String name;
	private String place;
	
	//constructor without time; for creating experiences
	public Experience(Long _id, String _exp, String _place, String _name) {
		this(_id, _exp, new java.util.Date(), _place, _name);
	}
	
	//constructor with time; for retrieving experiences from database
	public Experience(Long _id, String _exp, Date _time, String _place, String _name) {
		this.exp_id = _id;
		this.experience = _exp;
		this.time = _time;
		this.name = _name;
		this.place = _place;
	}
	
	public Experience() {
		this(null, null, null, null);
	}

	public Long getId() {
		return exp_id;
	}
	
	public String getExperience(){
		return experience;
	}
	
	public Date getDate() {
		return time;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPlace() {
		return place;
	}
	
	public void setId(Long id) {
		this.exp_id = id;
	}
	
	public void setExperience(String exp) {
		this.experience = exp;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setPlace(String place) {
		this.place = place;
	}
	
	@Override
	public boolean equals(Object that) {
		String[] exclude = {"experience", "time", "user", "place"};
		return EqualsBuilder.reflectionEquals(this, that, exclude);
	}
}
