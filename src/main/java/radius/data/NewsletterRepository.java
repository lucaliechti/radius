package radius.data;

import org.springframework.stereotype.Repository;
import radius.UserValidation;

import java.util.List;

@Repository
public interface NewsletterRepository {

    public void subscribe(String email, String source);

    public void unsubscribe(String uuid);

    public List<UserValidation> getRecipients();

    public int numberOfRecipients();

}
