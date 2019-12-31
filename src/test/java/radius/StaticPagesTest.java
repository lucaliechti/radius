package radius;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import radius.web.controller.StaticPagesController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringRunner.class)
@ComponentScan(basePackages = {"radius.config", "radius.security", "radius.data.repository", "radius.web"})
@SpringBootTest
public class StaticPagesTest {

    @Autowired
    StaticPagesController staticPagesController;

    @Test
    public void getAbout() throws Exception {
        MockMvc mockMvc = standaloneSetup(staticPagesController).build();
        mockMvc.perform(get("/about/"))
                .andExpect(view().name("about"));
    }
    @Test
    public void getImprint() throws Exception {
        MockMvc mockMvc = standaloneSetup(staticPagesController).build();
        mockMvc.perform(get("/imprint/"))
                .andExpect(view().name("imprint"));
    }
    @Test
    public void getPrivacy() throws Exception {
        MockMvc mockMvc = standaloneSetup(staticPagesController).build();
        mockMvc.perform(get("/privacy/"))
                .andExpect(view().name("privacy"));
    }
    @Test
    public void getSupport() throws Exception {
        MockMvc mockMvc = standaloneSetup(staticPagesController).build();
        mockMvc.perform(get("/support/"))
                .andExpect(view().name("support"));
    }
    @Test
    public void getMedia() throws Exception {
        MockMvc mockMvc = standaloneSetup(staticPagesController).build();
        mockMvc.perform(get("/media/"))
                .andExpect(view().name("media"));
    }
}
