package radius.data;

import org.springframework.stereotype.Repository;
import radius.User;
import radius.UserPair;
import radius.exceptions.EmailAlreadyExistsException;

import java.util.List;

@Repository
public interface UserRepository {

	public List<User> allUsers();

	public List<User> usersToMatch();
	
	public User findUserByEmail(String email);
	
	public void saveUser(User u) throws EmailAlreadyExistsException;
	
	public void updateUser(User u);
	
	public void enableUser(String email);
	
	public List<String> findAuthoritiesByEmail(String email);
	
	public boolean userExists(String email);
	
	public boolean userHasAnswered(String email);

	public void match(UserPair userPair);
	
	public boolean userIsEnabled(String email);
	
}
