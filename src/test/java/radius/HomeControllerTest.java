package radius;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;

import radius.data.JDBCStaticResourceRepository;
import radius.web.controller.HomeController;
import radius.web.controller.StatusController;

import java.util.Collections;

public class HomeControllerTest {

	@Test
	public void testHomePage() throws Exception {
        JDBCStaticResourceRepository staticRepo = mock(JDBCStaticResourceRepository.class);
		when(staticRepo.cantons()).thenReturn(Collections.emptyList());
		StatusController sc = mock(StatusController.class);

		HomeController controller = new HomeController(staticRepo, sc);
		MockMvc mockMvc = standaloneSetup(controller).build();
		mockMvc.perform(get("/")).andExpect(view().name("home"));
	}
}
