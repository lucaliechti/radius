package radius.data.repository;

import radius.data.form.MentionForm;

import java.util.List;

public interface MentionRepository {

    void addMention(MentionForm form);

    List<MentionForm> allMentions();

}
