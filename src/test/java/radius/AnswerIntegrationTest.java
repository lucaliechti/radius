package radius;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import radius.data.repository.JDBCUserRepository;
import radius.exceptions.EmailAlreadyExistsException;
import radius.web.controller.AnswerController;
import radius.web.controller.StatusController;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringRunner.class)
@ComponentScan(basePackages = {"radius.config", "radius.security", "radius.data.repository", "radius.web"})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@DataJdbcTest
public class AnswerIntegrationTest {

    private MockMvc mockMvc;

    JDBCUserRepository mockUserRepo = mock(JDBCUserRepository.class);
    User mockUser = mock(User.class);

    @Autowired
    AnswerController answerController;

    @Autowired
    StatusController statusController;

    @Before
    public void mockController() throws EmailAlreadyExistsException {
        ReflectionTestUtils.setField(answerController, "userRepo", mockUserRepo);
        ReflectionTestUtils.setField(statusController, "userRepo", mockUserRepo);
        doReturn(mockUser).when(mockUserRepo).findUserByEmail(anyString());
        doReturn(true).when(mockUser).isAnsweredRegular();
        doReturn(true).when(mockUser).isEnabled();

        doNothing().when(mockUserRepo).saveUser(any(User.class));
        doNothing().when(mockUserRepo).updateVotes(anyString(), anyString(), anyList());
    }

    @Test
    @WithMockUser
    public void getAnswers() throws Exception {
        mockMvc = standaloneSetup(answerController).build();

        mockMvc.perform(get("/answers/"))
                .andExpect(view().name("answers"));
    }

    @Test
    @WithMockUser
    public void emptyForm() throws Exception {
        mockMvc = standaloneSetup(answerController).build();

        mockMvc.perform(post("/answers/"))
                .andExpect(status().isOk())
                .andExpect(view().name("answers"));
    }

    @Test
    @WithMockUser
    public void onlyRegularQuestions() throws Exception {
        ReflectionTestUtils.setField(answerController, "specialIsActive", true);

        mockMvc = standaloneSetup(answerController).build();

        mockMvc.perform(post("/answers/")
                .param("regularanswers", "TRUE,FALSE,DONTCARE,TRUE,FALSE"))
                .andExpect(status().isOk())
                .andExpect(view().name("answers"));

        ReflectionTestUtils.setField(answerController, "specialIsActive", false);

        mockMvc.perform(post("/answers/")
                .param("regularanswers", "TRUE,FALSE,DONTCARE,TRUE,FALSE"))
                .andExpect(status().isOk())
                .andExpect(view().name("answers"));
    }

    @Test
    @WithMockUser
    public void validForm() throws Exception {
        mockMvc = standaloneSetup(answerController).build();

        mockMvc.perform(post("/answers")
                .param("regularanswers", "TRUE,FALSE,DONTCARE,TRUE,FALSE")
                .param("locations", "1;2;3")
                .param("languages", "DE,FR"))
                .andExpect(status().isOk())
                .andExpect(view().name("status"));
    }
}
