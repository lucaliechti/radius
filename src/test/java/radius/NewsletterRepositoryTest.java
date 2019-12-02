package radius;

import org.junit.Test;
import radius.data.repository.JDBCNewsletterRepository;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class NewsletterRepositoryTest {

    @Test
    public void generateRepo() {
        JDBCNewsletterRepository repo = mock(JDBCNewsletterRepository.class);

        when(repo.subscribe(anyString(), anyString())).thenReturn(UUID.randomUUID().toString());
        String uuid = repo.subscribe("hello@example.com", "TEST");

        verify(repo, times(1)).subscribe(anyString(), anyString());
        //assertEquals(UUID.randomUUID().toString().length(), uuid.length());
    }
}
