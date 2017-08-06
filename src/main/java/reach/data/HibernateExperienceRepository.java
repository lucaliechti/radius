package reach.data;

//DAO support: https://docs.spring.io/spring/docs/current/spring-framework-reference/html/dao.html

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import reach.Experience;

//@Repository
public class HibernateExperienceRepository implements ExperienceRepository {

	private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

	@Override
	public List<Experience> allExperiences() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Experience findExperienceById(long exp_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveExperience(Experience e) {
		// TODO Auto-generated method stub
		
	}

}
