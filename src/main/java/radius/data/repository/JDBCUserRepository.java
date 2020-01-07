package radius.data.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import radius.User;
import radius.exceptions.EmailAlreadyExistsException;
import radius.web.service.ConfigService;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class JDBCUserRepository implements UserRepository {

	private JdbcTemplate jdbcTemplate;
	private String currentVote;

	private static final String FIND_ALL_USERS =		"SELECT * FROM users LEFT JOIN (SELECT * FROM votes WHERE votenr = ?) AS currentvotes ON users.email = currentvotes.email ORDER BY users.pkey ASC";
	private static final String FIND_MATCHABLE_USERS =  "SELECT * FROM users LEFT JOIN (SELECT * FROM votes WHERE votenr = ?) AS currentvotes ON users.email = currentvotes.email WHERE status = 'WAITING' AND enabled = TRUE AND banned = FALSE";
	private static final String FIND_USER_BY_EMAIL = 	"SELECT * FROM users LEFT JOIN (SELECT * FROM votes WHERE votenr = ?) AS currentvotes ON users.email = currentvotes.email WHERE users.email = ?";
	private static final String FIND_USER_BY_UUID =		"SELECT email FROM users WHERE uuid = ?";
	private static final String FIND_UUID_BY_EMAIL = 	"SELECT uuid FROM users WHERE email = ?";
	private static final String SAVE_NEW_USER = 		"INSERT INTO users(datecreate, datemodify, firstname, lastname, canton, email, password, status, enabled, banned, uuid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, FALSE, ?)";
	private static final String UPDATE_USER = 			"UPDATE users SET status = ?, firstname = ?, lastname = ?, password = ?, canton = ?, languages = ?, locations = ?, enabled = ?, motivation = ?, datemodify = ?, uuid = ?, regularanswers = ? WHERE email = ?";
	private static final String CURRENT_VOTE_ANSWERED = "SELECT EXISTS (SELECT 1 FROM votes WHERE email = ? AND votenr = ?)";
	private static final String ANSWER_CURRENT_VOTE = 	"INSERT INTO votes (email, votenr, answer) VALUES (?, ?, ?)";
	private static final String UPDATE_VOTE =    		"UPDATE votes SET answer = ? WHERE email = ? AND votenr = ?";
	private static final String GRANT_USER_RIGHTS = 	"INSERT INTO authorities(datecreate, datemodify, email, authority) VALUES (?, ?, ?, ?)";
	private static final String FIND_AUTH_BY_EMAIL = 	"SELECT email, authority FROM authorities WHERE email = ?";
	private static final String USER_EXISTS = 			"SELECT EXISTS (SELECT 1 FROM users WHERE email = ?)";
	private static final String DELETE_AUTHORITIES =	"DELETE FROM authorities WHERE email = ?";
	private static final String DELETE_USER = 			"DELETE FROM users WHERE email = ?";
	private static final String UPDATE_LAST_LOGIN = 	"UPDATE users SET lastlogin = ? WHERE email = ?";
	private static final String REGION_DENSITY =		"SELECT locations FROM users";

	@Autowired
    public void init(DataSource jdbcdatasource, ConfigService configService) {
        this.jdbcTemplate = new JdbcTemplate(jdbcdatasource);
        this.currentVote = configService.specialActive() ? configService.currentVote() : "NO_VOTE";
    }

	@Override
	public List<User> allUsers() {
		return jdbcTemplate.query(FIND_ALL_USERS, new UserRowMapper(), currentVote);
	}

	@Override
	public List<User> matchableUsers() {
		return jdbcTemplate.query(FIND_MATCHABLE_USERS, new UserRowMapper(), currentVote);
	}

	@Override
	public User findUserByEmail(String email) throws EmptyResultDataAccessException {
		return jdbcTemplate.queryForObject(FIND_USER_BY_EMAIL, new UserRowMapper(), currentVote, email);
	}

	private static final class UserRowMapper implements RowMapper<User> {
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			List<Integer> locations = new ArrayList<>();
			if(rs.getString("locations") != null && !rs.getString("locations").equals("")){
				locations = Arrays.stream(rs.getString("locations").split(";")).map(Integer::valueOf).collect(Collectors.toList());
			}
			List<String> languages = new ArrayList<>();
			if(rs.getString("languages") != null && !rs.getString("languages").equals("")){
				languages = new ArrayList<String>(Arrays.asList(rs.getString("languages").split(";")));
			}
			List<String> regularanswers = new ArrayList<>();
			if(rs.getString("regularanswers") != null && !rs.getString("regularanswers").equals("")){
				regularanswers = new ArrayList<String>(Arrays.asList(rs.getString("regularanswers").split(";")));
			}
			List<String> specialanswers = new ArrayList<>();
			if(rs.getString("answer") != null && !rs.getString("answer").equals("")){
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
				rs.getBoolean("banned"),
				locations,
				languages,
				regularanswers,
				specialanswers,
				rs.getBoolean("private"),
				rs.getTimestamp("datemodify"),
				rs.getString("uuid")
			);
		}
	}

	@Override
	public void saveNewUser(User u) throws EmailAlreadyExistsException {
		if(userExists(u.getEmail())) {
			throw new EmailAlreadyExistsException("User with email " + u.getEmail() + " already exists.");
		}
		jdbcTemplate.update(SAVE_NEW_USER, OffsetDateTime.now(), OffsetDateTime.now(), u.getFirstname(),
				u.getLastname(), u.getCanton(), u.getEmail(), u.getPassword(), "INACTIVE", false, u.getUuid());
		grantUserRights(u.getEmail());
	}

	@Override
	public void updateExistingUser(User user) {
		String regularAnswers = user.getRegularanswers().stream().map(User::convertAnswerToString)
				.collect(Collectors.joining(";"));
		String lang = String.join(";", user.getLanguages());
		String loc = user.locationString();
		String status = user.getStatus().name();
		jdbcTemplate.update(UPDATE_USER, status, user.getFirstname(), user.getLastname(), user.getPassword(),
				user.getCanton(), lang, loc, user.isEnabled(), user.getMotivation(),
				OffsetDateTime.now(), user.getUuid(), regularAnswers, user.getEmail());
	}

	@Override
	public void updateVotes(String email, String currentVote, List<User.TernaryAnswer> answers) {
		String prettyAnswers = answers.stream().map(User::convertAnswerToString).collect(Collectors.joining(";"));
		boolean alreadyAnswered = jdbcTemplate.queryForObject(CURRENT_VOTE_ANSWERED, new Object[]{email, currentVote}, Boolean.class);
		if(alreadyAnswered) {
			jdbcTemplate.update(UPDATE_VOTE, prettyAnswers, email, currentVote);
		} else {
			jdbcTemplate.update(ANSWER_CURRENT_VOTE, email, currentVote, prettyAnswers);
		}
	}

	private void grantUserRights(String email){
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

	private boolean userExists(String email) {
		return jdbcTemplate.queryForObject(USER_EXISTS, new Object[]{email}, Boolean.class);
	}

	@Override
	public void deleteUser(String email) {
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

	@Override
	public void updateLastLogin(String name) {
		jdbcTemplate.update(UPDATE_LAST_LOGIN, OffsetDateTime.now(), name);
	}

	@Override
	public List<String> regionDensity() {
		List<Map<String, Object>> locations = jdbcTemplate.queryForList(REGION_DENSITY);
		return locations.stream().map(map -> (String) map.get("locations")).collect(Collectors.toList());
	}
}



