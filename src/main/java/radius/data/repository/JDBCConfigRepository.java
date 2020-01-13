package radius.data.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import radius.data.form.ConfigurationForm;
import radius.data.form.QuestionForm;

import javax.sql.DataSource;
import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@Repository
public class JDBCConfigRepository implements ConfigRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void init(DataSource jdbcdatasource) {
        this.jdbcTemplate = new JdbcTemplate(jdbcdatasource);
    }

    private static final String QUESTION_EXISTS = "SELECT EXISTS (SELECT 1 FROM questions WHERE identifier = ?)";
    private static final String GET_QUESTIONS = "SELECT questions FROM questions WHERE identifier = ?";
    private static final String SET_QUESTIONS = "UPDATE questions SET questions = ? WHERE identifier = ?";
    private static final String INSERT_QUESTIONS = "INSERT INTO questions (identifier, questions) VALUES (?, ?)";
    private static final String GET_CONFIGURATION_VALUE = "SELECT value FROM configuration WHERE key = ?";
    private static final String SET_CONFIGURATION_VALUE =  "UPDATE configuration SET value = ? WHERE key = ?";

    private static final String MATCHING_FACTOR_WAITING_TIME = "matching.factor.waitingtime";
    private static final String MATCHING_ACTIVE = "matching.active";
    private static final String MATCHING_MINIMUM_DISAGREEMENTS_REGULAR = "matching.minimum.disagreements.regular";
    private static final String MATCHING_MINIMUM_DISAGREEMENTS_SPECIAL = "matching.minimum.disagreements.special";

    private static final String CURRENT_SPECIAL_ACTIVE = "current.special.active";
    private static final String CURRENT_NUMBER_QUESTIONS_REGULAR = "current.number.questions.regular";
    private static final String CURRENT_NUMBER_QUESTIONS_SPECIAL = "current.number.questions.special";
    private static final String CURRENT_VOTE = "current.vote";

    private static final int NUMBER_LANGUAGES = 3;

    @Override
    public void updateConfiguration(ConfigurationForm form) throws SQLException {
        jdbcTemplate.update(SET_CONFIGURATION_VALUE, form.isMatchingFactorWaitingTime(), MATCHING_FACTOR_WAITING_TIME);
        jdbcTemplate.update(SET_CONFIGURATION_VALUE, form.isMatchingActive(), MATCHING_ACTIVE);
        jdbcTemplate.update(SET_CONFIGURATION_VALUE, form.getMatchingMinimumDisagreementsRegular(), MATCHING_MINIMUM_DISAGREEMENTS_REGULAR);
        jdbcTemplate.update(SET_CONFIGURATION_VALUE, form.getMatchingMinimumDisagreementsSpecial(), MATCHING_MINIMUM_DISAGREEMENTS_SPECIAL);

        jdbcTemplate.update(SET_CONFIGURATION_VALUE, form.isSpecialActive(), CURRENT_SPECIAL_ACTIVE);
        jdbcTemplate.update(SET_CONFIGURATION_VALUE, form.getNumberOfRegularQuestions(), CURRENT_NUMBER_QUESTIONS_REGULAR);
        jdbcTemplate.update(SET_CONFIGURATION_VALUE, form.getNumberOfVotes(), CURRENT_NUMBER_QUESTIONS_SPECIAL);
        jdbcTemplate.update(SET_CONFIGURATION_VALUE, form.getCurrentVote(), CURRENT_VOTE);
    }

    @Override
    public void updateQuestions(QuestionForm form) throws SQLException {
        ConfigurationForm config = getConfig();
        List<List<String>> regular = padOrCrop(form.getRegularQuestions(), config.getNumberOfRegularQuestions());
        jdbcTemplate.update(SET_QUESTIONS, convertToSQLArray(regular), "regular");
        if(config.isSpecialActive()) {
            List<List<String>> special = padOrCrop(form.getSpecialQuestions(), config.getNumberOfVotes());
            if (questionExists(config.getCurrentVote())) {
                jdbcTemplate.update(SET_QUESTIONS, convertToSQLArray(special), config.getCurrentVote());
            } else {
                jdbcTemplate.update(INSERT_QUESTIONS, config.getCurrentVote(), convertToSQLArray(special));
            }
        }
    }

    private List<List<String>> padOrCrop(List<List<String>> questions, int number) {
        for (int i = 0; i < NUMBER_LANGUAGES; i++) {
            if (questions.size() < NUMBER_LANGUAGES || questions.get(i) == null) {
                questions.add(new ArrayList<>());
            }
            int ithSize = questions.get(i).size();
            if (ithSize < number) {
                for (int j = 0; j < (number - ithSize); j++) {
                    questions.get(i).add("");
                }
            } else if (ithSize > number) {
                questions.set(i, new ArrayList<>(questions.get(i).subList(0, number)));
            }
        }
        return questions;
    }

    @Override
    public ConfigurationForm getConfig() throws SQLException {
        try {
            boolean waitingtime = getBoolean(MATCHING_FACTOR_WAITING_TIME);
            boolean matchingActive = getBoolean(MATCHING_ACTIVE);
            int disagreementsRegular =  getInt(MATCHING_MINIMUM_DISAGREEMENTS_REGULAR);
            int disagreementsSpecial =  getInt(MATCHING_MINIMUM_DISAGREEMENTS_SPECIAL);

            boolean specialActive = getBoolean(CURRENT_SPECIAL_ACTIVE);
            int questionsRegular = getInt(CURRENT_NUMBER_QUESTIONS_REGULAR);
            int questionsSpecial = getInt(CURRENT_NUMBER_QUESTIONS_SPECIAL);
            String currentVote = getString(CURRENT_VOTE);

            return new ConfigurationForm(waitingtime, matchingActive, disagreementsRegular, disagreementsSpecial,
                    specialActive, questionsSpecial, questionsRegular, currentVote);
        } catch (NullPointerException npe) {
            log.error("Parsing error when loading configuration");
            return null;
        }
    }

    @Override
    public QuestionForm getQuestions() throws SQLException {
        try {
            ConfigurationForm form = getConfig();
            List<List<String>> regularQuestions = get2DArray(GET_QUESTIONS, new Object[]{"regular"});
            List<List<String>> specialQuestions = form.isSpecialActive() ?
                    get2DArray(GET_QUESTIONS, new Object[]{form.getCurrentVote()}) : new ArrayList<>();
            return new QuestionForm(regularQuestions, specialQuestions);
        } catch (NullPointerException npe) {
            log.error("Parsing error when loading questions");
            return null;
        }
    }

    private boolean questionExists(String identifier) {
        return jdbcTemplate.queryForObject(QUESTION_EXISTS, new Object[]{identifier}, Boolean.class);
    }

    private boolean getBoolean(String key) {
        return Boolean.parseBoolean(jdbcTemplate.queryForObject(GET_CONFIGURATION_VALUE, new Object[]{key}, String.class));
    }

    private int getInt(String key) {
        return Integer.parseInt(Objects.requireNonNull(jdbcTemplate.queryForObject(GET_CONFIGURATION_VALUE, new Object[]{key}, String.class)));
    }

    private String getString(String key) {
        return jdbcTemplate.queryForObject(GET_CONFIGURATION_VALUE, new Object[]{key}, String.class);
    }

    private List<List<String>> get2DArray(String query, Object[] params) throws SQLException {
        try {
            Array result = jdbcTemplate.queryForObject(query, params, Array.class);
            String[][] questions = (String[][]) Objects.requireNonNull(result).getArray();
            List<List<String>> list = new ArrayList<>();
            for (String[] strings : questions) {
                list.add(new ArrayList<>(Arrays.asList(strings)));
            }
            return list;
        } catch (ClassCastException | EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    private Array convertToSQLArray(List<List<String>> input) throws SQLException {
        try (Connection c = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            String[][] array = input.stream()
                    .map(l -> l.stream().toArray(String[]::new))
                    .toArray(String[][]::new);
            return c.createArrayOf("varchar", array);
        }
    }

}
