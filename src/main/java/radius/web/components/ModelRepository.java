package radius.web.components;

import org.springframework.stereotype.Component;
import radius.data.dto.EmailDto;
import radius.data.form.UserForm;
import radius.data.repository.JSONStaticResourceRepository;
import radius.data.repository.StaticResourceRepository;

import java.util.HashMap;
import java.util.Map;

@Component
public class ModelRepository {

    private StaticResourceRepository staticRepo;

    public ModelRepository(JSONStaticResourceRepository staticRepo) {
        this.staticRepo = staticRepo;
    }

    public Map<String, Object> homeAttributes() {
        HashMap<String, Object> homeAttributes = new HashMap<>();
        homeAttributes.put("registrationForm", new UserForm());
        homeAttributes.put("cantons", staticRepo.cantons());
        homeAttributes.put("newsletterForm", new EmailDto());
        return homeAttributes;
    }

}
