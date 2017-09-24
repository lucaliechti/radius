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
	private static final String FIND_ALL_EXPERIENCES =	"SELECT * FROM experiences";
	private static final String FIND_EXPERIENCE_BY_ID =	"SELECT * FROM experiences WHERE pkey = ?";
	private static final String SAVE_EXPERIENCE = "INSERT INTO experiences(datecreate, datemodify, experience, user_email) VALUES (?, ?, ?, ?)";
	private static final String UPDATE_EXPERIENCE = "UPDATE experiences SET experience = ?, datemodify = now() WHERE pkey = ?";

    @Autowired
    public void init(DataSource jdbcdatasource) {
        this.jdbcTemplate = new JdbcTemplate(jdbcdatasource);
    }

	@Override
	public List<Experience> allExperiences() {
		return jdbcTemplate.query(FIND_ALL_EXPERIENCES, new ExperienceRowMapper());
	}
	
	//nothing to see here
	public Experience findExperienceById(long exp_id) {
		return jdbcTemplate.queryForObject(FIND_EXPERIENCE_BY_ID, new ExperienceRowMapper(), exp_id);
	}
	
	private static final class ExperienceRowMapper implements RowMapper<Experience> {
			public Experience mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Experience(
					rs.getLong("pkey"),
					rs.getString("experience"),
					rs.getDate("datecreate"),
					rs.getDate("datemodify"),
					rs.getString("user_email")
			);
		}
	}

	@Override
	public void saveNewExperience(Experience e) {
		jdbcTemplate.update(SAVE_EXPERIENCE, new java.util.Date(), new java.util.Date(), e.getExperience(), e.getUseremail());
		
	}
	
	@Override
	public void updateExperience(long id, Experience e) {
		jdbcTemplate.update(UPDATE_EXPERIENCE, e.getExperience(), new java.util.Date());
	}

	@Override
	public void deleteExperience(long id) {
		//stub, TODO
		
	}
    
}