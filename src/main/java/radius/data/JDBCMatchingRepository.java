package radius.data;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import radius.User;

@Repository
public class JDBCMatchingRepository implements MatchingRepository {
	
	@Autowired
	private UserRepository userRepo;

	private JdbcTemplate jdbcTemplate;
	private static final String GET_CURRENT_MATCH_EMAIL =		"SELECT email2 FROM matches WHERE email1 = ? AND active = TRUE";
	private static final String DEACTIVATE_OLD_MATCHES =	 	"UPDATE matches SET active = FALSE WHERE email1 = ?";
    
	@Autowired
    public void init(DataSource jdbcdatasource) {
        this.jdbcTemplate = new JdbcTemplate(jdbcdatasource);
    }
	
	@Override
	public User getCurrentMatchFor(String email) {
		String matchEmail = (String) jdbcTemplate.queryForObject(GET_CURRENT_MATCH_EMAIL, new Object[] { email }, String.class);
		return userRepo.findUserByEmail(matchEmail);
	}

	@Override
	public void deactiveOldMatchesFor(String email) {
		jdbcTemplate.update(DEACTIVATE_OLD_MATCHES, email);
	}

}
