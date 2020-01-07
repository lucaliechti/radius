package radius.data.repository;

import radius.data.form.ConfigurationForm;

import java.sql.SQLException;

public interface ConfigRepository {

    void updateConfig(ConfigurationForm form) throws SQLException;

    ConfigurationForm getConfig() throws SQLException;

}
