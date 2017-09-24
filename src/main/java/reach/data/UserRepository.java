package reach.data;

import java.util.List;

import org.springframework.stereotype.Repository;

import reach.User;

@Repository
public interface UserRepository {

	public List<User> allUsers();
	
	public User findUserByEmail(String email);
	
	public void saveUser(User u);
	
	public void updateUser(User u);
	
}
