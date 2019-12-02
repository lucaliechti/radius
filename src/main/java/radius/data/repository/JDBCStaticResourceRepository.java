package radius.data.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JDBCStaticResourceRepository implements StaticResourceRepository {

	private JdbcTemplate jdbcTemplate;
	private static final String FIND_ALL_CANTONS =	"SELECT code FROM cantons";
	private static final String FIND_ALL_REGIONS = 	"SELECT pkey, region FROM regions";
	
	//cache
	private List<String> cantons = null;
	private Map<Integer, String> regions = null;
	private List<String> languages = null;

    @Autowired
    public void init(DataSource jdbcdatasource) {
        this.jdbcTemplate = new JdbcTemplate(jdbcdatasource);
    }

	@Override
	public List<String> cantons() {
		return jdbcTemplate.query(FIND_ALL_CANTONS, new CantonRowMapper());
	}
	
	private static final class CantonRowMapper implements RowMapper<String> {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("code");
		}
	}

	@Override
	public Map<Integer, String> regions() {
		if(regions != null) {
			return regions;
		}
		List<Map<String, Object>> result = jdbcTemplate.queryForList(FIND_ALL_REGIONS);
		HashMap<Integer, String> map = new HashMap<>();

		for(Map<String, Object> resultmap : result) {
			try {
				int pkey = Integer.parseInt(resultmap.get("pkey").toString());
				String reg = resultmap.get("region").toString();
				map.put(pkey, reg);
			} catch (NumberFormatException nfe) {
				nfe.printStackTrace();
			}
		}
		return map;
	}

	@Override
	public List<String> languages() {
		return new ArrayList<>(Arrays.asList("DE", "FR", "IT", "EN"));
	}
	
	public List<String> prettyLocations(List<Integer> locs) {
		ArrayList<String> locations = new ArrayList<String>();
		for (int l : locs) {
			locations.add(regions().get(l));
		}
		return locations;
	}
}
