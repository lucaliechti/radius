package radius.data;

import java.util.List;

import org.springframework.stereotype.Repository;

import radius.User;
import radius.exceptions.EmailAlreadyExistsException;

@Repository
public interface UserRepository {

	public List<User> allUsers();
	
	public User findUserByEmail(String email);
	
	public void saveUser(User u) throws EmailAlreadyExistsException;
	
	public void updateUser(User u);
	
	public void enableUser(String email);
	
	public List<String> findAuthoritiesByEmail(String email);
	
	public boolean userExists(String email);
	
	public boolean userHasAnswered(String email);
	
	public boolean userIsEnabled(String email);
	
	public boolean userIsActive(String email);
	
	public void activateUser(String email);
	
	public void deactivateUser(String email);
	
	public void deleteUser(String email);
	
}
