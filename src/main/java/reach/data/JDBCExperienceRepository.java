package reach.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import reach.Experience;

//DAO support: https://docs.spring.io/spring/docs/current/spring-framework-reference/html/dao.html

@Repository
public class JDBCExperienceRepository implements ExperienceRepository {

	private JdbcTemplate jdbcTemplate;
	private static final String FIND_ALL_EXPERIENCES =	"SELECT exp_id, experience, exptime, place, name FROM experiences";
	private static final String FIND_EXPERIENCE_BY_ID =	"SELECT exp_id, experience, exptime, place, name FROM experiences WHERE exp_id = ?";
	private static final String SAVE_EXPERIENCE = "INSERT INTO experiences(exp_id, experience, exptime, place, name) VALUES (?, ?, ?, ?, ?)";

    @Autowired
    public void init(DataSource jdbcdatasource) {
        this.jdbcTemplate = new JdbcTemplate(jdbcdatasource);
    }

	@Override
	public List<Experience> allExperiences() {
		return jdbcTemplate.query(FIND_ALL_EXPERIENCES, new ExperienceRowMapper());
	}
	
	public Experience findExperienceById(long exp_id) {
		return jdbcTemplate.queryForObject(FIND_EXPERIENCE_BY_ID, new ExperienceRowMapper(), exp_id);
	}
	
	private static final class ExperienceRowMapper implements RowMapper<Experience> {
			public Experience mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Experience(
					rs.getLong("exp_id"),
					rs.getString("experience"),
					rs.getDate("exptime"),
					rs.getString("place"),
					rs.getString("name")
			);
		}
	}

	@Override
	public void saveExperience(Experience e) {
		jdbcTemplate.update(SAVE_EXPERIENCE, e.getId(), e.getExperience(), e.getDate(), e.getPlace(), e.getName());
		
	}
    
}
