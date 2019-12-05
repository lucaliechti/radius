package radius.web.service;

import org.springframework.stereotype.Service;
import radius.data.repository.JDBCSurveyRepository;
import radius.data.repository.SurveyRepository;

import java.util.List;

@Service
public class SurveyService {

    private SurveyRepository surveyRepository;

    public SurveyService(JDBCSurveyRepository surveyRepository) {
        this.surveyRepository = surveyRepository;
    }

    public boolean saveAnswers(List<Integer> questions, List<String> answers, boolean newsletter, boolean registration) {
        try {
            surveyRepository.saveAnswers(questions, answers, newsletter, registration);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
