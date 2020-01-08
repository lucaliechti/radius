package radius.data.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import radius.User;
import radius.exceptions.EmailAlreadyExistsException;
import radius.exceptions.UserHasMatchesException;

import java.util.List;

@Repository
public interface UserRepository {

	List<User> allUsers();

	List<User> matchableUsers();
	
	User findUserByEmail(String email) throws EmptyResultDataAccessException;
	
	void saveNewUser(User u) throws EmailAlreadyExistsException;
	
	void updateExistingUser(User u);

	void updateVotes(String email, String currentVote, List<User.TernaryAnswer> answers);

	List<String> findAuthoritiesByEmail(String email);

	void deleteUser(String email) throws UserHasMatchesException;

	String findEmailByUuid(String uuid) throws EmptyResultDataAccessException;

	String findUuidByEmail(String email) throws EmptyResultDataAccessException;

    void updateLastLogin(String name);

    List<String> regionDensity();

    void banUser(String username);
}
