package radius;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import radius.data.repository.JDBCNewsletterRepository;
import radius.data.repository.JDBCSurveyRepository;
import radius.web.controller.RegistrationController;
import radius.web.controller.SurveyController;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@RunWith(SpringRunner.class)
@ComponentScan(basePackages = {"radius.config", "radius.security", "radius.data.repository", "radius.web"})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@DataJdbcTest
public class SurveyIntegrationTest {

    private String correctAnswers = "TRUE,null,null,null,null,null,null,null,null,null,null,null,null,null,FALSE";
    private String correctQuestions = "1,15";
    private MockMvc mockMvc;

    JDBCSurveyRepository mockSurveyRepo = mock(JDBCSurveyRepository.class);
    JDBCNewsletterRepository mockNewsletterRepo = mock(JDBCNewsletterRepository.class);
    RegistrationController mockRegistrationController = mock(RegistrationController.class);

    @Autowired
    SurveyController surveyController;

    @Test
    public void getSurvey() throws Exception {
        mockMvc = standaloneSetup(surveyController).build();

        mockMvc.perform(get("/survey/"))
            .andExpect(view().name("survey"));
    }

    @Test
    public void postMinimalCorrectForm() throws Exception {
        mockMvc = standaloneSetup(surveyController).build();

        mockMvc.perform(post("/survey")
            .param("questions", correctQuestions)
            .param("newsletter", "false")
            .param("registration", "false"))
            .andExpect(status().isOk())
            .andExpect(view().name("home"));
    }

    @Test
    public void postMinimalIncorrectForm() throws Exception {
        mockMvc = standaloneSetup(surveyController).build();

        mockMvc.perform(post("/survey/")
            .param("answers", correctAnswers)
            .param("newsletter", "false")
            .param("registration", "false"))
            .andExpect(status().isOk())
            .andExpect(view().name("survey"));
    }

    @Test
    public void successfullyRegister() throws Exception {
        ReflectionTestUtils.setField(surveyController, "registrationController", mockRegistrationController);
        ReflectionTestUtils.setField(surveyController, "surveyRepo", mockSurveyRepo);
        doNothing().when(mockSurveyRepo).saveAnswers(anyList(), anyList(), anyBoolean(), anyBoolean());
        doReturn("home").when(mockRegistrationController).
                cleanlyRegisterNewUser(any(), any(), anyString(), anyString(), anyString(), anyString(), anyString());

        mockMvc = standaloneSetup(surveyController).build();

        mockMvc.perform(post("/survey")
            .param("questions", correctQuestions)
            .param("newsletter", "false")
            .param("registration", "true")
            .param("emailR", "luca.liechti@netcetera.com")
            .param("password", "veryverySecret")
            .param("firstName", "John")
            .param("lastName", "Doe")
            .param("canton", "NONE"))
            .andExpect(status().isOk())
            .andExpect(view().name("home"));
    }

    @Test
    public void failingSurveyRepo() throws Exception {
        ReflectionTestUtils.setField(surveyController, "surveyRepo", mockSurveyRepo);
        doThrow(RuntimeException.class).when(mockSurveyRepo).saveAnswers(anyList(), anyList(), anyBoolean(), anyBoolean());

        mockMvc = standaloneSetup(surveyController).build();

        mockMvc.perform(post("/survey/")
                .param("questions", correctQuestions)
                .param("newsletter", "false")
                .param("registration", "false"))
                .andExpect(status().isOk())
                .andExpect(view().name("survey"));
    }

    @Test
    public void successfullySubscribeToNewsletter() throws Exception {
        mockMvc = standaloneSetup(surveyController).build();

        mockMvc.perform(post("/survey")
            .param("questions", correctQuestions)
            .param("newsletter", "true")
            .param("emailN", "example@email.com")
            .param("registration", "false"))
            .andExpect(status().isOk())
            .andExpect(view().name("home"));
    }

    @Test
    public void failingNewsletterRepo() throws Exception {
        ReflectionTestUtils.setField(surveyController, "newsletterRepo", mockNewsletterRepo);
        doThrow(RuntimeException.class).when(mockNewsletterRepo).subscribe(anyString(), anyString());

        mockMvc = standaloneSetup(surveyController).build();

        mockMvc.perform(post("/survey/")
                .param("questions", correctQuestions)
                .param("newsletter", "true")
                .param("emailN", "some@email.com")
                .param("registration", "false"))
                .andExpect(status().isOk())
                .andExpect(view().name("survey"));
    }
}
