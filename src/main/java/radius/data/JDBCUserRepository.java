package radius.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import radius.User;
import radius.User.userStatus;

@Repository
public class JDBCUserRepository implements UserRepository{
	
	private JdbcTemplate jdbcTemplate;
	private static final String FIND_ALL_USERS =		"SELECT * FROM users";
	private static final String FIND_USER_BY_EMAIL = 	"SELECT email, password FROM users WHERE email = ?";
	private static final String SAVE_NEW_USER = 		"INSERT INTO users(datecreate, datemodify, firstname, lastname, canton, email, password, status, enabled) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String GRANT_USER_RIGHTS = 	"INSERT INTO authorities(datecreate, datemodify, email, authority) VALUES (?, ?, ?, ?)";
	private static final String FIND_AUTH_BY_EMAIL = 	"SELECT email, authority FROM authorities WHERE email = ?";
	
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
//			ArrayList<Boolean> questions = new ArrayList<Boolean>();
//			questions.add(rs.getBoolean("q1"));
//			questions.add(rs.getBoolean("q2"));
//			questions.add(rs.getBoolean("q3"));
//			questions.add(rs.getBoolean("q4"));
//			questions.add(rs.getBoolean("q5"));
			
			return new User(
				rs.getString("firstname"),
				rs.getString("lastname"),
				rs.getString("canton"),
				rs.getString("email"),
				rs.getString("password")
//				convertStatus(rs.getString("status")),
//				rs.getBoolean("enabled"),
//				rs.getBoolean("answered"),
//				questions
			);
		}

		private userStatus convertStatus(String status) {
			switch (status) {
				case "NEW":
					return userStatus.NEW;
				case "WAITING":
					return userStatus.WAITING;	
				case "MATCHED":
					return userStatus.MATCHED;
				case "INACTIVE":
					return userStatus.INACTIVE;
				default:
					return null;
			}
		}
	}
	
	//TODO: Check if username already exists etc.
	@Override
	public void saveUser(User u) {
		if(u.getCanton().equals("NONE")) {
			u.setCanton(null);
		}
		jdbcTemplate.update(SAVE_NEW_USER, OffsetDateTime.now(), OffsetDateTime.now(), u.getFirstname(), u.getLastname(), u.getCanton(), u.getEmail(), u.getPassword(), "INACTIVE", false);
		grantUserRights(u.getEmail());
	}
	
	@Override
	public void updateUser(User u) {
		// TODO Auto-generated method stub
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
}












