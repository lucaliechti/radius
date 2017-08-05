package reach;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;

public class Experience {
	private final Long exp_id;
	private final String experience;
	private final Date time;
	private final String name;
	private final String place;
	
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
	
	@Override
	public boolean equals(Object that) {
		String[] exclude = {"experience", "time", "user", "place"};
		return EqualsBuilder.reflectionEquals(this, that, exclude);
	}
}
