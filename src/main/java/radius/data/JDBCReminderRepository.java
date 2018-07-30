package radius.data;

import java.time.OffsetDateTime;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JDBCReminderRepository implements ReminderRepository {
	
	private JdbcTemplate jdbcTemplate;
	private static final String SAVE_REMINDER =	"INSERT INTO reminders (datecreate, email) VALUES (?, ?)";
	
    @Autowired
    public void init(DataSource jdbcdatasource) {
        this.jdbcTemplate = new JdbcTemplate(jdbcdatasource);
    }
    
	@Override
	public void saveReminder(String email) {
		jdbcTemplate.update(SAVE_REMINDER, OffsetDateTime.now(), email);
	}

}
