package radius.data.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class JDBCSurveyRepository implements SurveyRepository {

    private JdbcTemplate jdbcTemplate;

    public JDBCSurveyRepository(DataSource jdbcdatasource) {
        this.jdbcTemplate = new JdbcTemplate(jdbcdatasource);
    }

    private static final String SAVE_ANSWERS =  "INSERT INTO survey(datecreate, questions, answers, newsletter, registration) VALUES (?, ?, ?, ?, ?)";
    private static final String RESULTS = "SELECT questions, answers FROM survey";

    @Override
    public void saveAnswers(List<Integer> questions, List<String> answers, boolean newsletter, boolean registration) {
        String q = questions.stream().map(String::valueOf).collect(Collectors.joining(";"));
        String a = String.join(";", answers).replace("null", "");
        jdbcTemplate.update(SAVE_ANSWERS, OffsetDateTime.now(), q, a, newsletter, registration);
    }

    @Override
    public List<Map<Integer, Boolean>> results() {
        List<Map<Integer, Boolean>> statistics = new ArrayList<>();
        List<Map<String, Object>> results = jdbcTemplate.queryForList(RESULTS);
        for(Map<String, Object> filledOutSurvey : results) {
            Map<Integer, Boolean> thisSurvey = new HashMap<>();
            String questions = (String) filledOutSurvey.get("questions");
            String answers = (String) filledOutSurvey.get("answers");
            List<Integer> questionList = Arrays.stream(questions.split(";")).map(Integer::valueOf).collect(Collectors.toList());
            List<String> answerStrings = Arrays.stream(answers.split(";", -1)).collect(Collectors.toList());
            List<Boolean> answerList = new ArrayList<>();
            answerStrings.forEach(a -> answerList.add(a.equals("true") || a.equals("false") ? Boolean.valueOf(a) : null));
            questionList.forEach(i -> thisSurvey.put(i, answerList.get(i-1)));
            statistics.add(thisSurvey);
        }
        return statistics;
    }
}
