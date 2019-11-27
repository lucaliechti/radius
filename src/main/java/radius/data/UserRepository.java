package radius.data;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Repository;
import radius.User;
import radius.HalfEdge;
import radius.UserPair;
import radius.exceptions.EmailAlreadyExistsException;
import radius.exceptions.UserHasMatchesException;

import java.util.List;

@Repository
public interface UserRepository {

	public List<User> allUsers();

	public List<User> matchableUsers();
	
	public User findUserByEmail(String email);
	
	public void saveUser(User u) throws EmailAlreadyExistsException;
	
	public void updateUser(User u);

	public void grantUserRights(String email);

	public void updatePassword(String password, String uuid, String email);

	public List<HalfEdge> allMatchesForUser(String email);

	public void enableUser(String email);
	
	public List<String> findAuthoritiesByEmail(String email);
	
	public boolean userExists(String email);
	
	public boolean userHasAnswered(String email);

	public List<HalfEdge> allMatches();

	public void match(UserPair userPair);
	
	public boolean userIsEnabled(String email);
	
	public boolean userIsActive(String email);
	
	public void activateUser(String email);
	
	public void deactivateUser(String email);
	
	//TODO: It shouldn't be a problem if the user has matches. Make nice in the future.
	public void deleteUser(String email) throws UserHasMatchesException;

	public String findEmailByUuid(String uuid);

	public String findUuidByEmail(String email);
	
}
