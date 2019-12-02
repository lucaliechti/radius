package radius.data.repository;

import org.springframework.stereotype.Repository;
import radius.User;
import radius.HalfEdge;
import radius.UserPair;
import radius.exceptions.EmailAlreadyExistsException;
import radius.exceptions.UserHasMatchesException;

import java.util.List;

@Repository
public interface UserRepository {

	List<User> allUsers();

	List<User> matchableUsers();
	
	User findUserByEmail(String email);
	
	void saveUser(User u) throws EmailAlreadyExistsException;
	
	void updateUser(User u);

	void updateVotes(String email, String currentVote, List<User.TernaryAnswer> answers);

	void grantUserRights(String email);

	void updatePassword(String password, String uuid, String email);

	List<HalfEdge> allMatchesForUser(String email);

	void enableUser(String email);
	
	List<String> findAuthoritiesByEmail(String email);
	
	boolean userExists(String email);
	
	List<HalfEdge> allMatches();

	void match(UserPair userPair);

	boolean userIsActive(String email);
	
	void activateUser(String email);
	
	void deactivateUser(String email);

	void deleteUser(String email) throws UserHasMatchesException;

	String findEmailByUuid(String uuid);

	String findUuidByEmail(String email);
	
}
