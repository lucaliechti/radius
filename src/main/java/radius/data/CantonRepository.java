package radius.data;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface CantonRepository {
	public List<String> allCantons();

}
