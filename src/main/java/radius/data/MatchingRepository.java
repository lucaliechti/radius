package radius.data;

import radius.User;

public interface MatchingRepository {
	
	public User getCurrentMatchFor(String email);
	
	public void deactiveOldMatchesFor(String email);
	
}
