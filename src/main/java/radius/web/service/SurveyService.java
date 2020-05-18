package radius.web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import radius.data.repository.JDBCSurveyRepository;
import radius.data.repository.SurveyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class SurveyService {

    private final SurveyRepository surveyRepository;

    public boolean saveAnswers(List<Integer> questions, List<String> answers, boolean newsletter, boolean registration) {
        try {
            surveyRepository.saveAnswers(questions, answers, newsletter, registration);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public List<int[]> statistics() {
        final int POSITION_YES = 0;
        final int POSITION_NO = 1;
        final int POSITION_NOANSWER = 2;
        List<int[]> stats = new ArrayList<>();
        for(int i = 0; i < 15; i++) {
            stats.add(i, new int[]{0, 0, 0});
        }
        List<Map<Integer, Boolean>> results = surveyRepository.results();
        for(Map<Integer, Boolean> answer : results) {
            for(int n_question : answer.keySet()) {
                Boolean thisAnswer = answer.get(n_question);
                if(thisAnswer == null) {
                    stats.get(n_question-1)[POSITION_NOANSWER]++;
                } else if(thisAnswer == Boolean.TRUE) {
                    stats.get(n_question-1)[POSITION_YES]++;
                } else if(thisAnswer == Boolean.FALSE) {
                    stats.get(n_question-1)[POSITION_NO]++;
                } else {
                    stats.get(n_question-1)[POSITION_NOANSWER]++;
                }
            }
        }
        return stats;
    }
}
