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
    private RealWorldProperties realWorld;

    public ModelRepository(JSONStaticResourceRepository staticRepo, RealWorldProperties realWorld) {
        this.staticRepo = staticRepo;
        this.realWorld = realWorld;
    }

    public Map<String, Object> homeAttributes() {
        HashMap<String, Object> homeAttributes = new HashMap<>();
        homeAttributes.put("registrationForm", new UserForm());
        homeAttributes.put("cantons", staticRepo.cantons());
        homeAttributes.put("newsletterForm", new EmailDto());
        return homeAttributes;
    }

    public Map<String, Object> answerAttributes() {
        HashMap<String, Object> answerAttributes = new HashMap<>();
        answerAttributes.put("lang", staticRepo.languages());
        answerAttributes.put("nrQ", realWorld.getNumberOfRegularQuestions());
        answerAttributes.put("special", realWorld.isSpecialIsActive());
        answerAttributes.put("nrV", realWorld.getNumberOfVotes());
        answerAttributes.put("currentVote", realWorld.getCurrentVote());
        return answerAttributes;
    }

}
