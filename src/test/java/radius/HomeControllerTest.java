package radius;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.web.servlet.LocaleResolver;
import radius.web.components.ModelDecorator;
import radius.web.controller.HomeController;
import radius.web.service.AnswerService;
import radius.web.service.UserService;

public class HomeControllerTest {
	@Test
	public void testHomePage() throws Exception {
		UserService mockUserService = mock(UserService.class);
		AnswerService mockAnswerService = mock(AnswerService.class);
		ModelDecorator mockModelDecorator = mock(ModelDecorator.class);
		HomeController controller = new HomeController(mockUserService, mockAnswerService, mockModelDecorator);
		MockMvc mockMvc = standaloneSetup(controller).build();
		mockMvc.perform(get("/")).andExpect(view().name("home"));
	}
}
