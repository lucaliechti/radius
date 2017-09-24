package reach.data;

import java.util.List;

import org.springframework.stereotype.Repository;

import reach.Experience;

@Repository
public interface ExperienceRepository {
	
	public List<Experience> allExperiences();
	
	public Experience findExperienceById(long id);
	
	public void saveNewExperience(Experience e);
	
	public void updateExperience(long id, Experience e);
	
	public void deleteExperience(long id);
	
}
