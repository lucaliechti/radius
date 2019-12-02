package radius.data.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface StaticResourceRepository {

	List<String> cantons();
	
	List<String> languages();
	
	List<String> prettyLocations(List<Integer> locs);
}
