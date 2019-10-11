package radius;

import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import radius.data.JDBCUserRepository;
import radius.exceptions.EmailAlreadyExistsException;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@DataJdbcTest
@ComponentScan(basePackages = {"radius"})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Sql(scripts = "classpath:enter_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class EnteringUserTwiceTest {

    @Autowired
    private JDBCUserRepository repo;

    String alreadyExistingEmail = "alreadyExisting@example.com";

    /* Commenting in makes the test fail, as the user will be deleted.
    @Before
    @SneakyThrows
    public void deleteUsers() {
        repo.deleteUser(alreadyExistingEmail);
    } */

    @Test
    public void insertUser() throws Exception {
        User u = new User("firstname", "lastname", "NONE", alreadyExistingEmail, "secretSECRET");
        assertThrows(EmailAlreadyExistsException.class, () -> repo.saveUser(u));
    }

}
