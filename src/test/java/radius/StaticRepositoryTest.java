package radius;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import radius.data.repository.JSONStaticResourceRepository;
import radius.data.repository.StaticResourceRepository;

public class StaticRepositoryTest {
	
	@Autowired
	StaticResourceRepository r = new JSONStaticResourceRepository();

	@Test
	public void oneLanguage() {
		assertTrue(1 == 1);
	}
}
