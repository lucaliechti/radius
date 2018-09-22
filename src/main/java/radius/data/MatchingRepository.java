package radius.data;

import radius.User;

public interface MatchingRepository {
	
	public User getCurrentMatchFor(String email);
	
	public void deactivateOldMatchesFor(String email);
	
	public void confirmHalfEdge(String email);
	
	public void unconfirmHalfEdge(String email);
	
}
