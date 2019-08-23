package radius.data;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyRepository {

    public void saveAnswers(List<Integer> questions, List<Boolean> answers, boolean newsletter, boolean registration);

}
