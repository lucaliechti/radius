package reach.data;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import reach.Experience;

//DAO support: https://docs.spring.io/spring/docs/current/spring-framework-reference/html/dao.html

@Repository
public class JDBCExperienceRepository implements ExperienceRepository {

	private JdbcTemplate jdbcTemplate;

    @Autowired
    public void init(DataSource jdbcdatasource) {
        this.jdbcTemplate = new JdbcTemplate(jdbcdatasource);
    }

	@Override
	public List<Experience> allExperiences() {
		int rowCount = this.jdbcTemplate.queryForObject("select count(*) from reachtest", Integer.class);
		System.out.println("in the 'allExperiences' method of JDBCExperienceController. Found " + rowCount + " experiences" +
				" in the postgres database");
		return null;
	}
    
}
