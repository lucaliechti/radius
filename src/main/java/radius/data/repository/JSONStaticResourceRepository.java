package radius.data.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@PropertySource("classpath:config/static.properties")
public class JSONStaticResourceRepository implements StaticResourceRepository {

    @Value("${languages}")
    private List<String> languages;
    @Value("${cantons}")
    private List<String> cantons;
    @Value("#{${regions}}")
    private Map<Integer, String> regions;

    @Override
    public List<String> languages() {
        return languages;
    }

    @Override
    public List<String> cantons() {
        return cantons;
    }

    @Override
    public List<String> prettyLocations(List<Integer> locs) {
        return locs.stream().map(l -> regions.get(l)).collect(Collectors.toList());
    }
}
