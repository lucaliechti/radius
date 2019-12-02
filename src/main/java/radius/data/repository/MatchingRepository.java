package radius.data.repository;

import radius.User;

public interface MatchingRepository {
	
	User getCurrentMatchFor(String email);
	
	void deactivateOldMatchesFor(String email);
	
	void confirmHalfEdge(String email);
	
	void unconfirmHalfEdge(String email);
	
}
