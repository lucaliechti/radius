package radius.data.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import radius.HalfEdge;
import radius.MatchingMode;
import radius.User;
import radius.UserPair;

@Repository
public class JDBCMatchingRepository implements MatchingRepository {
	
	private UserRepository userRepo;
	private JdbcTemplate jdbcTemplate;

	private static final String GET_CURRENT_MATCH_EMAIL =	"SELECT email2 FROM matches WHERE email1 = ? AND active = TRUE";
	private static final String DEACTIVATE_OLD_MATCHES =	"UPDATE matches SET active = FALSE, dateinactive = ? WHERE email1 = ? AND active = TRUE";
	private static final String CONFIRM_HALF_EDGE = 		"UPDATE matches SET meetingconfirmed = TRUE WHERE email1 = ? AND active = TRUE";
	private static final String UNCONFIRM_HALF_EDGE = 		"UPDATE matches SET meetingconfirmed = FALSE WHERE email1 = ? AND active = TRUE";
	private static final String CREATE_MATCH =				"INSERT INTO matches(datecreated, email1, email2, active, meetingconfirmed, matchingmode) VALUES (?, ?, ?, TRUE, FALSE, ?)";
	private static final String ALL_MATCHES =				"SELECT * FROM matches";
	private static final String ALL_MATCHES_FOR_USER = 		"SELECT * FROM matches WHERE email1 = ?";
	private static final String INVALIDATE_MATCHES_FIRST =		"UPDATE matches SET email1 = 'DELETED' WHERE email1 = ?";
	private static final String INVALIDATE_MATCHES_SECOND =		"UPDATE matches SET email2 = 'DELETED' WHERE email2 = ?";

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

	@Override
	public List<HalfEdge> allMatches() {
		return jdbcTemplate.query(ALL_MATCHES, new MatchRowMapper());
	}

	@Override
	public List<HalfEdge> allMatchesForUser(String email) {
		return jdbcTemplate.query(ALL_MATCHES_FOR_USER, new MatchRowMapper(), email);
	}

	@Override
	public void createMatch(UserPair userPair, MatchingMode mode) {
		jdbcTemplate.update(CREATE_MATCH, OffsetDateTime.now(), userPair.getUser1().getEmail(),
				userPair.getUser2().getEmail(), mode.toString());
		jdbcTemplate.update(CREATE_MATCH, OffsetDateTime.now(), userPair.getUser2().getEmail(),
				userPair.getUser1().getEmail(), mode.toString());
	}

	@Override
	public void invalidateMatchesForUser(String email) {
		jdbcTemplate.update(INVALIDATE_MATCHES_FIRST, email);
		jdbcTemplate.update(INVALIDATE_MATCHES_SECOND, email);
	}

	private static final class MatchRowMapper implements RowMapper<HalfEdge> {
		public HalfEdge mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new HalfEdge(
					rs.getString("email1"),
					rs.getString("email2"),
					rs.getBoolean("active"),
					Optional.of(rs.getBoolean("meetingconfirmed")),
					rs.getTimestamp("datecreated"),
					Optional.ofNullable(rs.getTimestamp("dateinactive")),
					rs.getString("matchingmode")
			);
		}
	}

}
