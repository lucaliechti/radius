package radius;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import radius.data.form.UserForm;
import radius.web.controller.SurveyController;
import radius.web.service.NewsletterService;
import radius.web.service.SurveyService;
import radius.web.service.UserService;

import java.util.Locale;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@RunWith(SpringRunner.class)
@ComponentScan(basePackages = {"radius.config", "radius.security", "radius.data.repository", "radius.web"})
@WebMvcTest
public class SurveyIntegrationTest {

    private String correctAnswers = "TRUE,null,null,null,null,null,null,null,null,null,null,null,null,null,FALSE";
    private String correctQuestions = "1,15";
    private MockMvc mockMvc;

    SurveyService mockSurveyService = mock(SurveyService.class);
    NewsletterService mockNewsletterService = mock(NewsletterService.class);
    UserService mockUserService = mock(UserService.class);
    User mockUser = mock(User.class);

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
        ReflectionTestUtils.setField(surveyController, "surveyService", mockSurveyService);
        doReturn(true).when(mockSurveyService).saveAnswers(anyList(), anyList(), anyBoolean(), anyBoolean());
        ReflectionTestUtils.setField(surveyController, "userService", mockUserService);
        doReturn(true).when(mockUserService).registerNewUserFromUserForm(any(UserForm.class), any(Locale.class));

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
        ReflectionTestUtils.setField(surveyController, "surveyService", mockSurveyService);
        doReturn(false).when(mockSurveyService).saveAnswers(anyList(), anyList(), anyBoolean(), anyBoolean());

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
        ReflectionTestUtils.setField(surveyController, "newsletterService", mockNewsletterService);
        doReturn(false).when(mockNewsletterService).subscribe(anyString(), any(Locale.class), anyString());

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
