package reach.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import reach.User;
import reach.User.userStatus;

@Repository
public class JDBCUserRepository implements UserRepository{
	
	private JdbcTemplate jdbcTemplate;
	private static final String FIND_ALL_USERS =	"SELECT username, email FROM users";
	private static final String FIND_USER_BY_USERNAME =	"SELECT username, email FROM users WHERE username = ?";
	private static final String FIND_USER_BY_EMAIL = "SELECT username, email FROM users WHERE email = ?";
	private static final String SAVE_NEW_USER = "INSERT INTO users(username, password, enabled) VALUES (?, ?, ?)";
	private static final String GRANT_USER_RIGHTS = "INSERT INTO authorities(username, authority) VALUES (?, ?)";
	
    @Autowired
    public void init(DataSource jdbcdatasource) {
        this.jdbcTemplate = new JdbcTemplate(jdbcdatasource);
    }

	@Override
	public List<User> allUsers() {
		return jdbcTemplate.query(FIND_ALL_USERS, new UserRowMapper());
	}

	@Override
	public User findUserByUsername(String username) {
		return jdbcTemplate.queryForObject(FIND_USER_BY_USERNAME, new UserRowMapper(), username);
	}

	@Override
	public User findUserByEmail(String email) {
		return jdbcTemplate.queryForObject(FIND_USER_BY_EMAIL, new UserRowMapper(), email);
	}
	
	private static final class UserRowMapper implements RowMapper<User> {
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new User(
				rs.getString("username"),
				rs.getString("firstname"),
				rs.getString("lastname"),
				rs.getString("email"),
				rs.getString("location"),
				convertStatus(rs.getString("status")),
				rs.getBoolean("enabled"),
				rs.getBoolean("answered"),
				rs.getBoolean("q1"),
				rs.getBoolean("q2"),
				rs.getBoolean("q3"),
				rs.getBoolean("q4"),
				rs.getBoolean("q5")
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
		jdbcTemplate.update(SAVE_NEW_USER, u.getUsername(), u.getPassword(), false);
		grantUserRights(u.getUsername());
	}
	
	@Override
	public void updateUser(User u) {
		// TODO Auto-generated method stub
	}
	
	public void grantUserRights(String username){
		jdbcTemplate.update(GRANT_USER_RIGHTS, username, "ROLE_USER");
	}
}
