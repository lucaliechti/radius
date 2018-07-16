package radius.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JDBCCantonRepository implements CantonRepository {

	private JdbcTemplate jdbcTemplate;
	private static final String FIND_ALL_CANTONS =	"SELECT code FROM cantons";
	private List<String> cantons = null;

    @Autowired
    public void init(DataSource jdbcdatasource) {
        this.jdbcTemplate = new JdbcTemplate(jdbcdatasource);
    }

	@Override
	public List<String> allCantons() {
		if(cantons != null) { //home-made caching
			System.out.println("Loaded cantons fromc cache");
			return cantons;
		}
		return jdbcTemplate.query(FIND_ALL_CANTONS, new CantonRowMapper());
	}
	
	private static final class CantonRowMapper implements RowMapper<String> {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new String(
					rs.getString("code")
			);
		}
	}
}