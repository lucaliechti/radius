package radius.web.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import radius.HalfEdge;
import radius.User;
import radius.UserPair;
import radius.data.form.MeetingFeedbackForm;
import radius.data.repository.*;
import radius.exceptions.EmailAlreadyExistsException;
import radius.web.components.RealWorldProperties;

import java.util.*;

@Service
public class UserService {

    private UserRepository userRepo;
    private MatchingRepository matchRepo;
    private StaticResourceRepository staticRepo;
    private RealWorldProperties realWorld;

    public UserService(JDBCUserRepository userRepo, JDBCMatchingRepository matchRepo,
                       JSONStaticResourceRepository staticRepo, RealWorldProperties realWorld) {
        this.userRepo = userRepo;
        this.matchRepo = matchRepo;
        this.staticRepo = staticRepo;
        this.realWorld = realWorld;
    }

    public void activateUser(User user) {
        user.setStatus(User.UserStatus.WAITING);
        updateUser(user);
    }

    public void deactivateUser(User user) {
        user.setStatus(User.UserStatus.INACTIVE);
        updateUser(user);
    }

    public void updateUser(User user) {
        userRepo.updateUser(user);
        if(realWorld.isSpecialIsActive() && user.getSpecialanswers().size() > 0) {
            userRepo.updateVotes(user.getEmail(), realWorld.getCurrentVote(), user.getSpecialanswers());
        }
    }

    public void saveUser(User user) throws EmailAlreadyExistsException { //TODO: RegistrationController
        userRepo.saveUser(user);
    }

    public Optional<User> findUserByEmail(String email) {
        try {
            User user = userRepo.findUserByEmail(email);
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<String> findUuidByEmail(String email) {
        try {
            String uuid = userRepo.findUuidByEmail(email);
            return Optional.of(uuid);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<String> findEmailByUuid(String uuid) {
        try {
            String email = userRepo.findEmailByUuid(uuid);
            return Optional.of(email);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<HalfEdge> allMatchesForUser(String email) {
        return userRepo.allMatchesForUser(email);
    }

    public boolean updateUserAfterMeeting(User user, MeetingFeedbackForm feedbackForm) {
        switch(feedbackForm.getNextState()) {
            case "WAITING":
                activateUser(user);
                return true;
            case "INACTIVE":
                deactivateUser(user);
                return true;
            default:
                return false;
        }
    }

    public Map<String, Object> userSpecificAttributes(User user) {
        HashMap<String, Object> userSpecificAttributes = new HashMap<>();
        userSpecificAttributes.put("user", user);
        userSpecificAttributes.put("history", allMatchesForUser(user.getEmail()));
        userSpecificAttributes.put("feedbackForm", new MeetingFeedbackForm());
        userSpecificAttributes.put("userlocations", staticRepo.prettyLocations(user.getLocations()));
        if(user.getStatus() == User.UserStatus.MATCHED) {
            User match = matchRepo.getCurrentMatchFor(user.getEmail());
            userSpecificAttributes.put("match", match);
            UserPair up = UserPair.of(user, match);
            userSpecificAttributes.put("commonlocations",
                    String.join(", ", staticRepo.prettyLocations(new ArrayList<>(up.commonLocations()))));
            userSpecificAttributes.put("commonlanguages", up.commonLanguages());
        }
        return userSpecificAttributes;
    }
}
