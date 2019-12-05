package radius;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import radius.exceptions.EmailAlreadyExistsException;
import radius.web.components.RealWorldProperties;
import radius.web.controller.AnswerController;
import radius.web.service.UserService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringRunner.class)
@ComponentScan(basePackages = {"radius.config", "radius.security", "radius.data.repository", "radius.web"})
@WebMvcTest
public class AnswerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    AnswerController answerController;

    @Before
    public void mockController() throws EmailAlreadyExistsException {
        User mockUser = mock(User.class);
        UserService mockUserService = mock(UserService.class);
        doReturn(Optional.of(mockUser)).when(mockUserService).findUserByEmail(anyString());
        doReturn(true).when(mockUser).isAnsweredRegular();
        doReturn(true).when(mockUser).isEnabled();
        doNothing().when(mockUserService).saveUser(any(User.class));
        ReflectionTestUtils.setField(answerController, "userService", mockUserService);
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
        RealWorldProperties mockRealWorld = mock(RealWorldProperties.class);
        doReturn(true).when(mockRealWorld).isSpecialIsActive();
        ReflectionTestUtils.setField(answerController, "realWorld", mockRealWorld);

        mockMvc = standaloneSetup(answerController).build();

        mockMvc.perform(post("/answers/")
                .param("regularanswers", "TRUE,FALSE,DONTCARE,TRUE,FALSE"))
                .andExpect(status().isOk())
                .andExpect(view().name("answers"));

        doReturn(false).when(mockRealWorld).isSpecialIsActive();

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
