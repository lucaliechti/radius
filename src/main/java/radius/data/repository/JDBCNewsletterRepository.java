package radius.data.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.OffsetDateTime;
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
    private static final String NUMBER_OF_RECIPIENTS = "SELECT COUNT(*) FROM newsletter";
    private static final String ALREADY_SUBSCRIBED = "SELECT EXISTS (SELECT 1 FROM newsletter WHERE email = ?)";

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
    public int numberOfRecipients() {
        return jdbcTemplate.queryForObject(NUMBER_OF_RECIPIENTS, Integer.class);
    }

    @Override
    public boolean alreadySubscribed(String email) {
        return jdbcTemplate.queryForObject(ALREADY_SUBSCRIBED, new Object[]{email}, Boolean.class);
    }
}
