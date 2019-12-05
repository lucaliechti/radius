package radius.data.repository;

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

	private RealWorldProperties real;
	private JdbcTemplate jdbcTemplate;
	private String currentVote;

	private static final String FIND_ALL_USERS =		"SELECT * FROM users LEFT JOIN (SELECT * FROM votes WHERE votenr = ?) AS currentvotes ON users.email = currentvotes.email ORDER BY users.pkey ASC";
	private static final String FIND_MATCHABLE_USERS =  "SELECT * FROM users LEFT JOIN (SELECT * FROM votes WHERE votenr = ?) AS currentvotes ON users.email = currentvotes.email WHERE status = ? AND enabled = TRUE AND answered = TRUE AND banned = FALSE";
	private static final String FIND_USER_BY_EMAIL = 	"SELECT * FROM users LEFT JOIN (SELECT * FROM votes WHERE votenr = ?) AS currentvotes ON users.email = currentvotes.email WHERE users.email = ?";
	private static final String FIND_USER_BY_UUID =		"SELECT email FROM users WHERE uuid = ?";
	private static final String FIND_UUID_BY_EMAIL = 	"SELECT uuid FROM users WHERE email = ?";
	private static final String SAVE_NEW_USER = 		"INSERT INTO users(datecreate, datemodify, firstname, lastname, canton, email, password, status, answered, enabled, banned, uuid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, FALSE, ?)";
	private static final String UPDATE_USER = 			"UPDATE users SET status = ?, locations = ?, languages = ?, motivation = ?, answered = TRUE, regularanswers = ?, datemodify = ? WHERE email = ?";
	private static final String ANSWER_CURRENT_VOTE = 	"INSERT INTO votes (email, votenr, answer) VALUES (?, ?, ?)";
	private static final String UPDATE_VOTE =    		"UPDATE votes SET answer = ? WHERE email = ? AND votenr = ?";
	private static final String UPDATE_PASSWORD = 		"UPDATE users SET password = ?, uuid = ?, datemodify = ? WHERE email = ?";
	private static final String SET_USER_STATUS =		"UPDATE users SET status = ? WHERE email = ?";
	private static final String ENABLE_USER = 			"UPDATE users SET enabled = TRUE, uuid = ? WHERE email = ?";
	private static final String GRANT_USER_RIGHTS = 	"INSERT INTO authorities(datecreate, datemodify, email, authority) VALUES (?, ?, ?, ?)";
	private static final String FIND_AUTH_BY_EMAIL = 	"SELECT email, authority FROM authorities WHERE email = ?";
	private static final String USER_EXISTS = 			"SELECT EXISTS (SELECT 1 FROM users WHERE email = ?)";
	private static final String DELETE_AUTHORITIES =	"DELETE FROM authorities WHERE email = ?";
	private static final String DELETE_USER = 			"DELETE FROM users WHERE email = ?";
	private static final String CREATE_MATCH =			"INSERT INTO matches(datecreated, email1, email2, active, meetingconfirmed) VALUES (?, ?, ?, TRUE, FALSE)";
	private static final String ALL_MATCHES =			"SELECT * FROM matches";
	private static final String ALL_MATCHES_FOR_USER = 	"SELECT * FROM matches WHERE email1 = ?";

	@Autowired
    public void init(DataSource jdbcdatasource, RealWorldProperties prop) {
        this.jdbcTemplate = new JdbcTemplate(jdbcdatasource);
        this.real = prop;
        this.currentVote = real.isSpecialIsActive() ? real.getCurrentVote() : "NO_VOTE";
    }

	@Override
	public List<User> allUsers() {
		return jdbcTemplate.query(FIND_ALL_USERS, new UserRowMapper(), currentVote);
	}

	@Override
	public List<User> matchableUsers() {
		return jdbcTemplate.query(FIND_MATCHABLE_USERS, new UserRowMapper(), currentVote, User.UserStatus.WAITING);
	}

	@Override
	public User findUserByEmail(String email) throws EmptyResultDataAccessException {
		return jdbcTemplate.queryForObject(FIND_USER_BY_EMAIL, new UserRowMapper(), currentVote, email);
	}

	//TODO: Use java.util.Optional
	//TODO: Use arrays on DB
	private static final class UserRowMapper implements RowMapper<User> {
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			List<Integer> locations = Collections.emptyList();
			if(rs.getString("locations") != null){
				locations = Arrays.stream(rs.getString("locations").split(";")).map(Integer::valueOf).collect(Collectors.toList());
			}
			ArrayList<String> languages = new ArrayList<String>();
			if(rs.getString("languages") != null){
				languages = new ArrayList<String>(Arrays.asList(rs.getString("languages").split(";")));
			}
			ArrayList<String> regularanswers = new ArrayList<String>();
			if(rs.getString("regularanswers") != null){
				regularanswers = new ArrayList<String>(Arrays.asList(rs.getString("regularanswers").split(";")));
			}
			ArrayList<String> specialanswers = new ArrayList<String>();
			if(rs.getString("answer") != null){
				specialanswers = new ArrayList<String>(Arrays.asList(rs.getString("answer").split(";")));
			}

			return new User(
				rs.getString("firstname"),
				rs.getString("lastname"),
				rs.getString("email"),
				rs.getString("password"),
				rs.getString("canton"),
				User.convertStatus(rs.getString("status")),
				rs.getString("motivation"),
				rs.getBoolean("enabled"),
				rs.getBoolean("answered"),
				rs.getBoolean("banned"),
				locations,
				languages,
				regularanswers,
				specialanswers,
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
				Optional.of(rs.getBoolean("meetingconfirmed")),
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
		jdbcTemplate.update(SAVE_NEW_USER, OffsetDateTime.now(), OffsetDateTime.now(), u.getFirstname(),
				u.getLastname(), u.getCanton() == "NONE" ? null : u.getCanton(), u.getEmail(), u.getPassword(), "INACTIVE", false, false, u.getUuid());
		grantUserRights(u.getEmail());
	}

	@Override
	public void updateUser(User u) {
		String regularAnswers = String.join(";", u.getRegularAnswersAsListOfStrings());
		String lang = String.join(";", u.getLanguages());
		String loc = User.createLocString(u.getLocations());
		String status = u.getStatus().name();
		jdbcTemplate.update(UPDATE_USER, status, loc, lang, u.getMotivation(), regularAnswers, OffsetDateTime.now(),
				u.getEmail());
	}

	@Override
	public void updateVotes(String email, String currentVote, List<User.TernaryAnswer> answers) {
		String prettyAnswers = answers.stream().map(User::convertAnswerToString).collect(Collectors.joining(";"));
		try {
			jdbcTemplate.update(UPDATE_VOTE, prettyAnswers, email, currentVote);
		} catch (EmptyResultDataAccessException er) { //TODO: don't do program logic with exceptions
			jdbcTemplate.update(ANSWER_CURRENT_VOTE, email, currentVote, prettyAnswers);
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
		return jdbcTemplate.queryForObject(USER_EXISTS, new Object[]{email}, Boolean.class);
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
		jdbcTemplate.update(ENABLE_USER, UUID.randomUUID().toString(), email);
	}

	@Override
	public void match(UserPair userPair) {
		jdbcTemplate.update(SET_USER_STATUS, User.convertStatusToString(User.UserStatus.MATCHED), userPair.user1().getEmail());
		jdbcTemplate.update(SET_USER_STATUS, User.convertStatusToString(User.UserStatus.MATCHED), userPair.user2().getEmail());
		jdbcTemplate.update(CREATE_MATCH, OffsetDateTime.now(), userPair.user1().getEmail(), userPair.user2().getEmail());
		jdbcTemplate.update(CREATE_MATCH, OffsetDateTime.now(), userPair.user2().getEmail(), userPair.user1().getEmail());
	}

	@Override
	public void deleteUser(String email) throws UserHasMatchesException {
		List<HalfEdge> matches = jdbcTemplate.query(ALL_MATCHES_FOR_USER, new MatchRowMapper(), email);
		if(matches.size() > 0){
			throw new UserHasMatchesException(email + " has matches and cannot be deleted.");
		}
		jdbcTemplate.update(DELETE_AUTHORITIES, email);
		jdbcTemplate.update(DELETE_USER, email);
	}

	@Override
	public String findEmailByUuid(String uuid) throws EmptyResultDataAccessException {
		return jdbcTemplate.queryForObject(FIND_USER_BY_UUID, String.class, uuid);
	}

	@Override
	public String findUuidByEmail(String email) throws EmptyResultDataAccessException {
		return jdbcTemplate.queryForObject(FIND_UUID_BY_EMAIL, String.class, email);
	}
}



