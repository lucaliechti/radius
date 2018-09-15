package radius.data;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JDBCUUIDRepository implements UUIDRepository {
	
	private JdbcTemplate jdbcTemplate;
	private static final String INSERT_UUID =			"INSERT INTO uuids (email, uuid) VALUES (?, ?)";
	private static final String REMOVE_USER = 			"DELETE FROM uuids WHERE email = ?";
	private static final String FIND_USER_BY_UUID = 	"SELECT email FROM uuids WHERE uuid = ?";
	
    @Autowired
    public void init(DataSource jdbcdatasource) {
        this.jdbcTemplate = new JdbcTemplate(jdbcdatasource);
    }
    
	@Override
	public void insertUUID(String email, String uuid) {
		jdbcTemplate.update(INSERT_UUID, email, uuid);
	}

	@Override
	public void removeUser(String email) {
		jdbcTemplate.update(REMOVE_USER, email);
	}

	@Override
	public String findUserByUUID(String uuid) {
		String email = (String) jdbcTemplate.queryForObject(FIND_USER_BY_UUID, new Object[] { uuid }, String.class);
		return email;
	}

}
