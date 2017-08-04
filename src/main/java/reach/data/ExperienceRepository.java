package reach.data;

import java.util.List;

import org.springframework.stereotype.Repository;

import reach.Experience;

@Repository
public interface ExperienceRepository {
	
	public List<Experience> allExperiences();
	
}
