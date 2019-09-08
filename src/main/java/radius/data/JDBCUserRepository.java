package radius.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import radius.HalfEdge;
import radius.User;
import radius.UserPair;
import radius.exceptions.EmailAlreadyExistsException;
import radius.exceptions.UserHasMatchesException;
import radius.web.components.RealWorldProperties;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class JDBCUserRepository implements UserRepository {

	@Autowired
	private RealWorldProperties real;
	
	private JdbcTemplate jdbcTemplate;
	private static final String FIND_ALL_USERS =		"SELECT * FROM users";
	private static final String FIND_USERS_TO_MATCH =   "SELECT * FROM users WHERE status = ? AND enabled = TRUE AND answered = TRUE AND banned = FALSE";
	private static final String FIND_USER_BY_EMAIL = 	"SELECT * FROM users WHERE email = ?";
	private static final String FIND_CURRENT_ANSWERS = 	"SELECT answer FROM votes WHERE email = ? AND votenr = ?";
	private static final String FIND_USER_BY_UUID =		"SELECT * FROM users WHERE uuid = ?";
	private static final String FIND_UUID_BY_EMAIL = 	"SELECT uuid FROM users WHERE email = ?";
	private static final String SAVE_NEW_USER = 		"INSERT INTO users(datecreate, datemodify, firstname, lastname, canton, email, password, status, answered, enabled, banned, uuid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, FALSE, ?)";
	private static final String UPDATE_USER = 			"UPDATE users SET locations = ?, languages = ?, motivation = ?, modus = ?, answered = TRUE, regularanswers = ?, datemodify = ? WHERE email = ?";
	private static final String ANSWER_CURRENT = 		"INSERT INTO votes (email, votenr, answer) VALUES (?, ?, ?)";
	private static final String UPDATE_CURRENT = 		"UPDATE votes SET answer = ? WHERE email = ? AND votenr = ?";
	private static final String UPDATE_PASSWORD = 		"UPDATE users SET password = ?, uuid = ?, datemodify = ? WHERE email = ?";
	private static final String GRANT_USER_RIGHTS = 	"INSERT INTO authorities(datecreate, datemodify, email, authority) VALUES (?, ?, ?, ?)";
	private static final String FIND_AUTH_BY_EMAIL = 	"SELECT email, authority FROM authorities WHERE email = ?";
	private static final String USER_EXISTS = 			"SELECT EXISTS (SELECT 1 FROM users WHERE email = ?)";
	private static final String USER_ANSWERED = 		"SELECT EXISTS (SELECT 1 FROM users WHERE email = ? AND answered = TRUE)";
	private static final String USER_ENABLED = 			"SELECT EXISTS (SELECT 1 FROM users WHERE email = ? AND enabled = TRUE)";
	private static final String USER_ACTIVE = 			"SELECT EXISTS (SELECT 1 FROM users WHERE email = ? AND NOT status = 'INACTIVE')";
	private static final String ENABLE_USER = 			"UPDATE users SET enabled = TRUE WHERE email = ?";
	private static final String DELETE_AUTHORITIES =	"DELETE FROM authorities WHERE email = ?";
	private static final String DELETE_USER = 			"DELETE FROM users WHERE email = ?";

	private static final String SET_USER_STATUS =		"UPDATE users SET status = ? WHERE email = ?";
	private static final String CREATE_MATCH =			"INSERT INTO matches(datecreated, email1, email2, active, meetingconfirmed) VALUES (?, ?, ?, TRUE, FALSE)";

	private static final String ALL_MATCHES =			"SELECT * FROM matches";
	private static final String ALL_MATCHES_FOR_USER = 	"SELECT * FROM matches WHERE email1 = ?";

	@Autowired
    public void init(DataSource jdbcdatasource) {
        this.jdbcTemplate = new JdbcTemplate(jdbcdatasource);
    }

	@Override
	public List<User> allUsers() {
		return jdbcTemplate.query(FIND_ALL_USERS, new UserRowMapper());
	}

	@Override
	public List<User> usersToMatch() {
		return jdbcTemplate.query(FIND_USERS_TO_MATCH, new UserRowMapper(), User.convertStatusToString(User.userStatus.WAITING));
	}

	@Override
	public User findUserByEmail(String email) {
		User u;
		try {
			u = jdbcTemplate.queryForObject(FIND_USER_BY_EMAIL, new UserRowMapper(), email);
		} catch (EmptyResultDataAccessException e) {
			System.out.println("UserRepository: No user with email = " + email);
			return null;
		}
		try { //it can very well be that everthing is in order with the user and they just haven't filled out the vote questions
			Map<String, Object> result = jdbcTemplate.queryForMap(FIND_CURRENT_ANSWERS, email, real.currentVote());
			u.setSpecialanswers(Arrays.asList(result.get("answer").toString().split(";")));
		}
		catch (Exception e) {
			u.setSpecialanswers(Collections.emptyList());
			System.out.println("UserRepository: Either no current vote or user has not answered the special questions");
		}
		return u;
	}

	private static final class UserRowMapper implements RowMapper<User> {
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			String email = rs.getString("email");
			List locations = Arrays.asList(rs.getString("locations").split(";")).stream().map(Integer::valueOf).collect(Collectors.toList());
			ArrayList<String> languages = new ArrayList<String>();
			if(rs.getString("languages") != null){
				languages = new ArrayList<String>(Arrays.asList(rs.getString("languages").split(";")));
			}

			return new User(
					rs.getString("firstname"),
					rs.getString("lastname"),
					email,
					rs.getString("password"),
					rs.getString("canton"),
					User.convertModus(rs.getString("modus")),
					User.convertStatus(rs.getString("status")),
					rs.getString("motivation"),
					rs.getBoolean("enabled"),
					rs.getBoolean("answered"),
					rs.getBoolean("banned"),
					locations,
					languages,
					Arrays.asList(rs.getString("regularanswers").split(";")),
//					List.of("TRUE", "FALSE", "DONTCARE"),
					rs.getTimestamp("datemodify")
			);
		}
	}

	private static final class MatchRowMapper implements RowMapper<HalfEdge> {
		public HalfEdge mapRow(ResultSet rs, int rowNum) throws SQLException {
			return HalfEdge.of(
					rs.getString("email1"),
					rs.getString("email2"),
					rs.getBoolean("active"),
					Optional.ofNullable(rs.getBoolean("meetingconfirmed")),
					rs.getTimestamp("datecreated"),
					Optional.ofNullable(rs.getTimestamp("dateinactive"))
			);
		}
	}
	
	@Override
	public void saveUser(User u) throws EmailAlreadyExistsException {
		if(userExists(u.getEmail())) {
			throw new EmailAlreadyExistsException("User with email " + u.getEmail() + " already exists.");
		}
		if(u.getCanton().equals("NONE")) {
			u.setCanton(null);
		}
		
		jdbcTemplate.update(SAVE_NEW_USER, OffsetDateTime.now(), OffsetDateTime.now(), u.getFirstname(), u.getLastname(), u.getCanton(), u.getEmail(), u.getPassword(), "INACTIVE", false, false, u.getUuid());
		grantUserRights(u.getEmail());
	}

	@Override
	public void updateUser(User u) {
		String regularAnswers = String.join(";", u.getRegularAnswersAsListOfStrings());
		String lang = String.join(";", u.getLanguages());
		String loc = User.createLocString(u.getLocations());
		jdbcTemplate.update(UPDATE_USER, loc, lang, u.getMotivation(), User.convertModusToString(u.getModus()),
				regularAnswers, OffsetDateTime.now(), u.getEmail());
	}

	public void updateVotes(String email, String currentVote, List<User.answer> answers) {
		String prettyanswers = answers.stream().map(a -> User.convertAnswerToString(a)).collect(Collectors.joining(";"));
		try {
			Map<String, Object> result = jdbcTemplate.queryForMap(FIND_CURRENT_ANSWERS, email, real.currentVote());
			jdbcTemplate.update(UPDATE_CURRENT, prettyanswers, email, currentVote);
		}
		catch (EmptyResultDataAccessException er) {
			jdbcTemplate.update(ANSWER_CURRENT, email, currentVote, prettyanswers);
		}
	}

	@Override
	public void updatePassword(String password, String uuid, String email) {
		jdbcTemplate.update(UPDATE_PASSWORD, password, uuid, OffsetDateTime.now(), email);
	}

	@Override
	public void grantUserRights(String email){
		jdbcTemplate.update(GRANT_USER_RIGHTS, OffsetDateTime.now(), OffsetDateTime.now(), email, "ROLE_USER");
	}

	@Override
	public List<String> findAuthoritiesByEmail(String email) {
		ArrayList<String> roles = new ArrayList<String>();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(FIND_AUTH_BY_EMAIL, email); 
		for (Map<String, Object> row: rows) {
			roles.add(row.get("authority").toString());
		}
		return roles;
	}

	@Override
	public boolean userExists(String email) {
		// super ugly
		Map<String, Object> users = jdbcTemplate.queryForMap(USER_EXISTS, email);
		return (boolean)users.get("exists");
	}

	@Override
	public boolean userHasAnswered(String email) {
		// also super ugly
		Map<String, Object> users = jdbcTemplate.queryForMap(USER_ANSWERED, email);
		return (boolean)users.get("exists");
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
	public void enableUser(String email) {
		jdbcTemplate.update(ENABLE_USER, email);
	}

	@Override
	public boolean userIsEnabled(String email) {
		// guess what, also super ugly
		Map<String, Object> users = jdbcTemplate.queryForMap(USER_ENABLED, email);
		return (boolean)users.get("exists");
	}

	@Override
	public void match(UserPair userPair) {
		jdbcTemplate.update(SET_USER_STATUS, User.convertStatusToString(User.userStatus.MATCHED), userPair.user1().getEmail());
		jdbcTemplate.update(SET_USER_STATUS, User.convertStatusToString(User.userStatus.MATCHED), userPair.user2().getEmail());
		jdbcTemplate.update(CREATE_MATCH, OffsetDateTime.now(), userPair.user1().getEmail(), userPair.user2().getEmail());
		jdbcTemplate.update(CREATE_MATCH, OffsetDateTime.now(), userPair.user2().getEmail(), userPair.user1().getEmail());
	}

	@Override
	public boolean userIsActive(String email) {
		// still plenty of chances to do it the ugly way
		Map<String, Object> users = jdbcTemplate.queryForMap(USER_ACTIVE, email);
		return (boolean)users.get("exists");
	}

	@Override
	public void activateUser(String email) {
		jdbcTemplate.update(SET_USER_STATUS, "WAITING", email);
	}

	@Override
	public void deactivateUser(String email) {
		jdbcTemplate.update(SET_USER_STATUS, "INACTIVE", email);
	}

	@Override
	public void deleteUser(String email) throws UserHasMatchesException {
		//TODO: This has quite some implications - make nice
		List<HalfEdge> matches = jdbcTemplate.query(ALL_MATCHES_FOR_USER, new MatchRowMapper(), email);
		if(matches.size()!=0){
			throw new UserHasMatchesException(email + " has matches and cannot be deleted.");
		}
		jdbcTemplate.update(DELETE_AUTHORITIES, email);
		jdbcTemplate.update(DELETE_USER, email);
	}

	@Override
	public User findUserByUuid(String uuid) {
		try {
			return jdbcTemplate.queryForObject(FIND_USER_BY_UUID, new UserRowMapper(), uuid);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public String findUuidByEmail(String email) {
		try {
			Map<String, Object> result = jdbcTemplate.queryForMap(FIND_UUID_BY_EMAIL, email);
			return result.get("uuid").toString();
		}
		catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
}



