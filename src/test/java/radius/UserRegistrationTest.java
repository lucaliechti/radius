package radius;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import radius.data.repository.JDBCUserRepository;

@RunWith(SpringRunner.class)
@DataJdbcTest
@ComponentScan(basePackages = {"radius"})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class UserRegistrationTest {

        @Autowired
        private JDBCUserRepository repo;

        @Test
        public void insertUser() throws Exception {
            String examplemail = "email@example.com";

            User saved = new User("firstname", "lastname", "NONE", examplemail, "secretSECRET");
            repo.saveUser(saved);
            User compared = repo.findUserByEmail(examplemail);
            assertThat(saved.getEmail()).isEqualTo(compared.getEmail());
        }
}
