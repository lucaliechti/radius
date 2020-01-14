package radius.data.repository;

import radius.HalfEdge;
import radius.MatchingMode;
import radius.User;
import radius.UserPair;

import java.util.List;

public interface MatchingRepository {
	
	User getCurrentMatchFor(String email);
	
	void deactivateOldMatchesFor(String email);
	
	void confirmHalfEdge(String email);
	
	void unconfirmHalfEdge(String email);

	List<HalfEdge> allMatches();

	List<HalfEdge> allMatchesForUser(String email);

	void createMatch(UserPair userPair, MatchingMode mode);

	void invalidateMatchesForUser(String username);
	
}
