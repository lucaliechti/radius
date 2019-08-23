package radius.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import radius.UserValidation;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public class JDBCNewsletterRepository implements NewsletterRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void init(DataSource jdbcdatasource) {
        this.jdbcTemplate = new JdbcTemplate(jdbcdatasource);
    }

    private static final String SUBSCRIBE =  "INSERT INTO newsletter(datecreate, email, source, uuid) VALUES (?, ?, ?, ?)";
    private static final String UNSUBSCRIBE = "DELETE FROM newsletter WHERE uuid = ?";
    private static final String GET_RECIPIENTS = "SELECT email, uuid FROM newsletter";

    @Override
    public void subscribe(String email, String source) {
        jdbcTemplate.update(SUBSCRIBE, OffsetDateTime.now(), email, source, UUID.randomUUID().toString());
    }

    @Override
    public void unsubscribe(String uuid) {
        jdbcTemplate.update(UNSUBSCRIBE, uuid);
    }

    @Override
    public List<UserValidation> getRecipients() {
        return jdbcTemplate.query(GET_RECIPIENTS, new UserValidationRowMapper());
    }

    @Override
    public int numberOfRecipients() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM newsletter", Integer.class);
    }

    private static final class UserValidationRowMapper implements RowMapper<UserValidation> {
        public UserValidation mapRow(ResultSet rs, int rowNum) throws SQLException {
            String email = rs.getString("email");
            String uuid = rs.getString("uuid");

            return new UserValidation(email, uuid);
        }
    }
}
