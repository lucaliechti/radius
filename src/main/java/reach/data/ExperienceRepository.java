package reach.data;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import reach.Experience;

@Repository
public class ExperienceRepository { //will be an interface
	
	public ExperienceRepository() {
		
	}
	
	public List<Experience> allExperiences() {
		System.out.print("In the ExperienceRepo. Adding ");
		List<Experience> experiences = new ArrayList<Experience>();
		Experience exp1 = new Experience(1l, "1st experience");
		Experience exp2 = new Experience(2l, "2nd experience");
		Experience exp3 = new Experience(3l, "3rd experience");
		experiences.add(exp1);
		experiences.add(exp2);
		experiences.add(exp3);
		System.out.println(experiences.size() + " experiences manually");
		
		return experiences;
	}
}
