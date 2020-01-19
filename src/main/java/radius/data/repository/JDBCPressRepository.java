package radius.data.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import radius.data.dto.PressreleaseDto;
import radius.data.form.MentionForm;

import javax.sql.DataSource;
import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Repository
public class JDBCPressRepository implements PressRepository {

    private JdbcTemplate jdbcTemplate;

    private static final String ADD_MENTION = "INSERT INTO mentions (publicationDate, medium, link, publicationType, publicationLanguage, uuid) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String ALL_MENTIONS = "SELECT * FROM mentions ORDER BY publicationDate DESC";
    private static final String ADD_PRESSRELEASE = "INSERT INTO pressreleases (releasedate, links) VALUES (?, ?)";
    private static final String ALL_PRESSRELEASES = "SELECT * FROM pressreleases ORDER BY releasedate DESC";

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

    @Override
    public void addPressrelease(PressreleaseDto dto) throws SQLException {
        jdbcTemplate.update(ADD_PRESSRELEASE, dto.getDate(), convertToSQLArray(dto.getLinks()));
    }

    @Override
    public List<PressreleaseDto> allPressReleases() {
        try {
            return jdbcTemplate.query(ALL_PRESSRELEASES, new PressreleaseRowMapper());
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

    private static final class PressreleaseRowMapper implements RowMapper<PressreleaseDto> {
        public PressreleaseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            PressreleaseDto dto = new PressreleaseDto();
            dto.setDate(rs.getDate("releasedate"));
            dto.setLinks((String[]) rs.getArray("links").getArray());
            return dto;
        }
    }

    private Array convertToSQLArray(String[] input) throws SQLException {
        try (Connection c = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            return c.createArrayOf("varchar", input);
        }
    }

}
