package radius.data.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface NewsletterRepository {

    String subscribe(String email, String source);

    void unsubscribe(String uuid);

    int numberOfRecipients();

    boolean alreadySubscribed(String email);

}
