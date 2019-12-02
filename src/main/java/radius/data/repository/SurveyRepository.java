package radius.data.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyRepository {

    void saveAnswers(List<Integer> questions, List<String> answers, boolean newsletter, boolean registration);

}
