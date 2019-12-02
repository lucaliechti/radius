package radius.data.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JDBCSurveyRepository implements SurveyRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void init(DataSource jdbcdatasource) {
        this.jdbcTemplate = new JdbcTemplate(jdbcdatasource);
    }

    private static final String SAVE_ANSWERS =  "INSERT INTO survey(datecreate, questions, answers, newsletter, registration) VALUES (?, ?, ?, ?, ?)";

    @Override
    public void saveAnswers(List<Integer> questions, List<String> answers, boolean newsletter, boolean registration) {
        String q = questions.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(";"));
        String a = answers.stream()
                .collect(Collectors.joining(";"))
                .replace("null", "");
        jdbcTemplate.update(SAVE_ANSWERS, OffsetDateTime.now(), q, a, newsletter, registration);
    }
}
