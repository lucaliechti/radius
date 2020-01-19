package radius.web.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import radius.data.dto.PressreleaseDto;
import radius.data.form.MentionForm;
import radius.data.repository.JDBCPressRepository;
import radius.data.repository.PressRepository;

import java.sql.SQLException;
import java.util.List;

@Slf4j
@Service
public class PressService {

    private PressRepository pressRepo;

    public PressService(JDBCPressRepository pressRepo) {
        this.pressRepo = pressRepo;
    }

    public void addMention(MentionForm form) {
        pressRepo.addMention(form);
    }

    public List<MentionForm> allMentions() {
        return pressRepo.allMentions();
    }

    public void addPressrelease(PressreleaseDto dto) {
        try {
            pressRepo.addPressrelease(dto);
            log.info("Successfully saved new press release");
        } catch (SQLException sqle) {
            log.error("Error saving new press release");
        }
    }

    public List<PressreleaseDto> allPressreleases() {
        return pressRepo.allPressReleases();
    }

}
