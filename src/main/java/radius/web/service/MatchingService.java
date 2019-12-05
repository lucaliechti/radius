package radius.web.service;

import org.springframework.stereotype.Service;
import radius.User;
import radius.data.form.MeetingFeedbackForm;
import radius.data.repository.MatchingRepository;

@Service
public class MatchingService {

    private MatchingRepository matchRepo;

    public MatchingService(MatchingRepository matchRepo) {
        this.matchRepo = matchRepo;
    }

    public void deactivateOldMatchesFor(String email) {
        matchRepo.deactivateOldMatchesFor(email);
    }

    public void updateMatchesAfterMeeting(User user, MeetingFeedbackForm feedbackForm) {
        if(feedbackForm.isConfirmed()) {
            confirmHalfEdge(user.getEmail());
        } else {
            unconfirmHalfEdge(user.getEmail());
        }
        deactivateOldMatchesFor(user.getEmail());
    }

    private void confirmHalfEdge(String email) {
        matchRepo.confirmHalfEdge(email);
    }

    private void unconfirmHalfEdge(String email) {
        matchRepo.unconfirmHalfEdge(email);
    }

}
