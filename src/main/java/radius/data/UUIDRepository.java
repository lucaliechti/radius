package radius.data;

public interface UUIDRepository {
	public void insertUUID(String email, String uuid);
	
	public void removeUser(String email);
	
	public String findUserByUUID(String uuid);
}
