package radius.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import radius.User;
import radius.exceptions.EmailAlreadyExistsException;

@Repository
public class JDBCUserRepository implements UserRepository {
	
	private JdbcTemplate jdbcTemplate;
	private static final String FIND_ALL_USERS =		"SELECT * FROM users";
	private static final String FIND_USER_BY_EMAIL = 	"SELECT * FROM users WHERE email = ?";
	private static final String SAVE_NEW_USER = 		"INSERT INTO users(datecreate, datemodify, firstname, lastname, canton, email, password, status, answered, enabled) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String UPDATE_USER = 			"UPDATE users SET locations = ?, languages = ?, motivation = ?, modus = ?, answered = TRUE, q1 = ?, q2 = ?, q3 = ?, q4 = ?, q5 = ?, datemodify = ? WHERE email = ?";
	private static final String GRANT_USER_RIGHTS = 	"INSERT INTO authorities(datecreate, datemodify, email, authority) VALUES (?, ?, ?, ?)";
	private static final String FIND_AUTH_BY_EMAIL = 	"SELECT email, authority FROM authorities WHERE email = ?";
	private static final String USER_EXISTS = 			"SELECT EXISTS (SELECT 1 FROM users WHERE email = ?)";
	private static final String USER_ANSWERED = 		"SELECT EXISTS (SELECT 1 FROM users WHERE email = ? AND answered = TRUE)";
	
    @Autowired
    public void init(DataSource jdbcdatasource) {
        this.jdbcTemplate = new JdbcTemplate(jdbcdatasource);
    }

	@Override
	public List<User> allUsers() {
		return jdbcTemplate.query(FIND_ALL_USERS, new UserRowMapper());
	}

	@Override
	public User findUserByEmail(String email) {
		return jdbcTemplate.queryForObject(FIND_USER_BY_EMAIL, new UserRowMapper(), email);
	}
	
	private static final class UserRowMapper implements RowMapper<User> {
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			String email = rs.getString("email");
			ArrayList<Boolean> questions = new ArrayList<Boolean>();
			questions.add(rs.getBoolean("q1"));
			questions.add(rs.getBoolean("q2"));
			questions.add(rs.getBoolean("q3"));
			questions.add(rs.getBoolean("q4"));
			questions.add(rs.getBoolean("q5"));

			List<Integer> locations = null;
			String[] loc = new String[0];
			String rs_loc = rs.getString("locations");
			if(rs_loc != null) {
				loc = rs.getString("locations").split(";");
			}
			Integer[] locint = new Integer[loc.length];
			try {
				for(int i = 0; i < loc.length; i++) {
					locint[i] = Integer.parseInt(loc[i]);
				}
			}
			catch(NumberFormatException nfe) {nfe.printStackTrace();} 
			locations = Arrays.asList(locint);
			System.out.println(loc);
			ArrayList<String> languages = new ArrayList<String>(Arrays.asList(rs.getString("languages").split(";")));

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
				locations,
				languages,
				questions
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
		
		jdbcTemplate.update(SAVE_NEW_USER, OffsetDateTime.now(), OffsetDateTime.now(), u.getFirstname(), u.getLastname(), u.getCanton(), u.getEmail(), u.getPassword(), "INACTIVE", false, false);
		grantUserRights(u.getEmail());
	}
	
	//TODO: locations
	@Override
	public void updateUser(User u) {
		List<Boolean> questions = u.getQuestions();
		String lang = String.join(";", u.getLanguages());
		String loc = "";
		for(Integer locid : u.getLocations()) {
			if(loc==""){
				loc += locid;//.toString();
			}
			else {
				loc += ";"+locid;
			}
		}
		//String loc = String.join(";", u.getLocations());
		jdbcTemplate.update(UPDATE_USER, loc, lang, u.getMotivation(), User.convertModusToString(u.getModus()), 
				questions.get(0), questions.get(1), questions.get(2), questions.get(3), 
				questions.get(4), OffsetDateTime.now(), u.getEmail());
	}
	
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
	public void enableUser(String email) {
		// TODO Auto-generated method stub
		
	}
}



