package radius;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import radius.web.components.CountrySpecificProperties;

import java.util.Arrays;
import java.util.Collections;

@RunWith(SpringRunner.class)
@ComponentScan(basePackages = {"radius.data.repository"})
@WebMvcTest
public class StaticRepositoryTest {

	private final int NUMBER_OF_LANGUAGES = 4;
	private final int NUMBER_OF_CANTONS = 26;
	
	@Autowired
    CountrySpecificProperties r = new CountrySpecificProperties();

	@Test
	public void languages() {
		assertNotNull(r.getLanguages());
		assertEquals(NUMBER_OF_LANGUAGES, r.getLanguages().size());
		assertTrue(r.getLanguages().contains("DE"));
	}

	@Test
	public void cantons() {
		assertNotNull(r.getCantons());
		assertEquals(NUMBER_OF_CANTONS, r.getCantons().size());
		assertTrue(r.getCantons().contains("BE"));
		assertFalse(r.getCantons().contains("ZZ"));
	}

	@Test
	public void prettyLocations() {
		assertTrue(r.prettyLocations(Collections.singletonList(1)).contains("Zürich"));
		assertEquals(3, r.prettyLocations(Arrays.asList(1,2,3)).size());
		assertEquals(0, r.prettyLocations(Collections.emptyList()).size());
	}
}
