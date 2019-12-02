package radius.data.repository;

import org.springframework.stereotype.Repository;
import radius.data.dto.EmailUuidDto;

import java.util.List;

@Repository
public interface NewsletterRepository {

    String subscribe(String email, String source);

    void unsubscribe(String uuid);

    List<EmailUuidDto> getRecipients();

    int numberOfRecipients();

    boolean alreadySubscribed(String email);

}
