package radius.data.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface StaticResourceRepository {

	List<String> cantons();

	Map<Integer, String> regions();
	
	List<String> languages();
	
	List<String> prettyLocations(List<Integer> locs);
}
