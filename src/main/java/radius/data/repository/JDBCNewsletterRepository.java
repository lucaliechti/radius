package radius.data.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import radius.data.dto.EmailSourceDto;

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
    private static final String ALL_RECIPIENTS = "SELECT * FROM newsletter";
    private static final String ALREADY_SUBSCRIBED = "SELECT EXISTS (SELECT 1 FROM newsletter WHERE email = ?)";
    private static final String UUID_FOR_EMAIL = "SELECT uuid FROM newsletter WHERE email = ?";

    @Override
    public String subscribe(String email, String source) {
        String uuid = UUID.randomUUID().toString();
        jdbcTemplate.update(SUBSCRIBE, OffsetDateTime.now(), email, source, uuid);
        return uuid;
    }

    @Override
    public void unsubscribe(String uuid) {
        jdbcTemplate.update(UNSUBSCRIBE, uuid);
    }

    @Override
    public boolean alreadySubscribed(String email) {
        return jdbcTemplate.queryForObject(ALREADY_SUBSCRIBED, new Object[]{email}, Boolean.class);
    }

    @Override
    public List<EmailSourceDto> allRecipients() {
        return jdbcTemplate.query(ALL_RECIPIENTS, new JDBCNewsletterRepository.NewsletterDtoRowMapper());
    }

    @Override
    public String findUuidByEmail(String email) {
        return jdbcTemplate.queryForObject(UUID_FOR_EMAIL, new Object[]{email}, String.class);
    }

    private static final class NewsletterDtoRowMapper implements RowMapper<EmailSourceDto> {
        public EmailSourceDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new EmailSourceDto(
                rs.getString("email"),
                rs.getString("source"),
                rs.getString("uuid")
            );
        }
    }
}
