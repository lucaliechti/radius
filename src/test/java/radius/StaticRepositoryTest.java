package radius;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import radius.data.repository.JSONStaticResourceRepository;

import java.util.Arrays;
import java.util.Collections;

@RunWith(SpringRunner.class)
@ComponentScan(basePackages = {"radius.web"})
@SpringBootTest
public class StaticRepositoryTest {

	private final int NUMBER_OF_LANGUAGES = 4;
	private final int NUMBER_OF_CANTONS = 26;
	
	@Autowired
	JSONStaticResourceRepository r = new JSONStaticResourceRepository();

	@Test
	public void languages() {
		assertNotNull(r.languages());
		assertEquals(NUMBER_OF_LANGUAGES, r.languages().size());
		assertTrue(r.languages().contains("DE"));
	}

	@Test
	public void cantons() {
		assertNotNull(r.cantons());
		assertEquals(NUMBER_OF_CANTONS, r.cantons().size());
		assertTrue(r.cantons().contains("BE"));
		assertFalse(r.cantons().contains("ZZ"));
	}

	@Test
	public void prettyLocations() {
		assertTrue(r.prettyLocations(Collections.singletonList(1)).contains("Zürich"));
		assertEquals(3, r.prettyLocations(Arrays.asList(1,2,3)).size());
		assertEquals(0, r.prettyLocations(Collections.emptyList()).size());
	}
}
