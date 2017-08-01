package reach;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;

public class Experience {
	private final Long exp_id;
	private final String experience;
	private final Date time;
	
	public Experience() {
		this(null, null);
	}
	
	public Experience(String _exp) {
		this(null, _exp);
		
	}
	
	public Experience(Long _id, String _exp) {
		this.exp_id = _id;
		this.experience = _exp;
		this.time = new java.util.Date();
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
	
	@Override
	public boolean equals(Object that) {
		String[] exclude = {"experience", "time"};
		return EqualsBuilder.reflectionEquals(this, that, exclude);
	}
}
