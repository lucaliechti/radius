package radius.web.service;

import org.springframework.stereotype.Service;
import radius.data.form.MentionForm;
import radius.data.repository.MentionRepository;

import java.util.List;

@Service
public class MentionService {

    private MentionRepository mentionRepo;

    public MentionService(MentionRepository mentionRepo) {
        this.mentionRepo = mentionRepo;
    }

    public void addMention(MentionForm form) {
        mentionRepo.addMention(form);
    }

    public List<MentionForm> allMentions() {
        return mentionRepo.allMentions();
    }

}
