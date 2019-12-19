package radius.data.repository;

import org.springframework.stereotype.Repository;
import radius.data.dto.EmailSourceDto;

import java.util.List;

@Repository
public interface NewsletterRepository {

    String subscribe(String email, String source);

    void unsubscribe(String uuid);

    boolean alreadySubscribed(String email);

    List<EmailSourceDto> allRecipients();

    String findUuidByEmail(String email);

}
