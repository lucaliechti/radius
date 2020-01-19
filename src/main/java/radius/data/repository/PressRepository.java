package radius.data.repository;

import org.springframework.stereotype.Repository;
import radius.data.dto.PressreleaseDto;
import radius.data.form.MentionForm;

import java.sql.SQLException;
import java.util.List;

@Repository
public interface PressRepository {

    void addMention(MentionForm form);

    List<MentionForm> allMentions();

    void addPressrelease(PressreleaseDto dto) throws SQLException;

    List<PressreleaseDto> allPressReleases();

}
