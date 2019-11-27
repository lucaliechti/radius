package radius.data;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface StaticResourceRepository {
	public List<String> cantons();

	public Map<Integer, String> regions();
	
	public List<String> languages();
	
	public List<String> prettyLocations(List<Integer> locs);
}
