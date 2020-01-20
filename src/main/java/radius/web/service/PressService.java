package radius.web.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import radius.data.dto.NewsDto;
import radius.data.dto.PressreleaseDto;
import radius.data.form.MentionForm;
import radius.data.form.NewsForm;
import radius.data.repository.JDBCPressRepository;
import radius.data.repository.PressRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    public void addNews(NewsForm form) {
        sanitizeTexts(form);
        try {
            pressRepo.saveNews(form);
            log.info("Successfully saved news article");
        } catch (SQLException sqle) {
            log.error("Error saving news article");
        }
    }

    private void sanitizeTexts(NewsForm form) {
        String[] sanitized = new String[form.getTexts().length];
        for(int i = 0; i < form.getTexts().length; i++) {
            sanitized[i] = form.getTexts()[i].replace("\n", "<br>");
        }
        form.setTexts(sanitized);
    }

    public List<NewsDto> allNews(Locale loc) {
        List<NewsForm> news = pressRepo.allNews();
        return languageSpecificNews(news, loc);
    }

    public List<NewsDto> languageSpecificNews(List<NewsForm> news, Locale loc) {
        int position = loc.getLanguage().equals("de") ? 0 : loc.getLanguage().equals("fr") ? 1 : 2;
        ArrayList<NewsDto> dtos = new ArrayList<>();
        news.forEach(n -> dtos.add(new NewsDto(n.getDate(), n.getTitles()[position], n.getTexts()[position])));
        return dtos;
    }

}
