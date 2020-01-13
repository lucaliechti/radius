package radius.data.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import radius.data.form.MentionForm;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Repository
public class JDBCMentionRepository implements MentionRepository {

    private JdbcTemplate jdbcTemplate;

    private static final String ADD_MENTION = "INSERT INTO mentions (publicationDate, medium, link, publicationType, publicationLanguage, uuid) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String ALL_MENTIONS = "SELECT * FROM mentions ORDER BY publicationDate DESC";

    @Autowired
    public void init(DataSource jdbcdatasource) {
        this.jdbcTemplate = new JdbcTemplate(jdbcdatasource);
    }

    @Override
    public void addMention(MentionForm form) {
        jdbcTemplate.update(ADD_MENTION, form.getDate(), form.getMedium(), form.getLink(), form.getType(),
                form.getLanguage(), UUID.randomUUID().toString());
    }

    @Override
    public List<MentionForm> allMentions() {
        try {
            return jdbcTemplate.query(ALL_MENTIONS, new MentionRowMapper());
        } catch (Exception e ) {
            return Collections.emptyList();
        }
    }

    private static final class MentionRowMapper implements RowMapper<MentionForm> {
        public MentionForm mapRow(ResultSet rs, int rowNum) throws SQLException {
            MentionForm form = new MentionForm();
            form.setDate(rs.getDate("publicationdate"));
            form.setMedium(rs.getString("medium"));
            form.setLink(rs.getString("link"));
            form.setType(rs.getString("publicationtype"));
            form.setLanguage(rs.getString("publicationlanguage"));
            return form;
        }
    }

}
