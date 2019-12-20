package radius.data.repository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SurveyRepository {

    void saveAnswers(List<Integer> questions, List<String> answers, boolean newsletter, boolean registration);

    List<Map<Integer, Boolean>> results();

}
