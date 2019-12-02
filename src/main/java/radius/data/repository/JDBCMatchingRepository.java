package radius.data.repository;

import java.time.OffsetDateTime;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import radius.User;

@Repository
public class JDBCMatchingRepository implements MatchingRepository {
	
	private UserRepository userRepo;
	private JdbcTemplate jdbcTemplate;

	private static final String GET_CURRENT_MATCH_EMAIL =		"SELECT email2 FROM matches WHERE email1 = ? AND active = TRUE";
	private static final String DEACTIVATE_OLD_MATCHES =	 	"UPDATE matches SET active = FALSE, dateinactive = ? WHERE email1 = ?";
	private static final String CONFIRM_HALF_EDGE = 			"UPDATE matches SET meetingconfirmed = TRUE WHERE email1 = ? AND active = TRUE";
	private static final String UNCONFIRM_HALF_EDGE = 			"UPDATE matches SET meetingconfirmed = FALSE WHERE email1 = ? AND active = TRUE";
    
	@Autowired
    public void init(DataSource jdbcdatasource, JDBCUserRepository jdbcUserRepository) {
        this.jdbcTemplate = new JdbcTemplate(jdbcdatasource);
        this.userRepo = jdbcUserRepository;
    }
	
	@Override
	public User getCurrentMatchFor(String email) {
		String matchEmail = jdbcTemplate.queryForObject(GET_CURRENT_MATCH_EMAIL, new Object[] { email }, String.class);
		return userRepo.findUserByEmail(matchEmail);
	}

	@Override
	public void deactivateOldMatchesFor(String email) {
		jdbcTemplate.update(DEACTIVATE_OLD_MATCHES, OffsetDateTime.now(), email);
	}
	
	@Override
	public void confirmHalfEdge(String email) {
		jdbcTemplate.update(CONFIRM_HALF_EDGE, email);
	}
	
	@Override
	public void unconfirmHalfEdge(String email) {
		jdbcTemplate.update(UNCONFIRM_HALF_EDGE, email);
	}

}
