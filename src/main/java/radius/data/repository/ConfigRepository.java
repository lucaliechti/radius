package radius.data.repository;

import radius.data.form.ConfigurationForm;
import radius.data.form.QuestionForm;

import java.sql.SQLException;

public interface ConfigRepository {

    void updateConfiguration(ConfigurationForm form) throws SQLException;

    ConfigurationForm getConfig() throws SQLException;

    void updateQuestions(QuestionForm form) throws SQLException;

    QuestionForm getQuestions() throws SQLException;

}
