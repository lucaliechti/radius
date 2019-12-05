package radius.web.components;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Component
@PropertySource("classpath:config/static.properties")
public class CountrySpecificProperties {

    @Value("${languages}")
    private List<String> languages;
    @Value("${cantons}")
    private List<String> cantons;
    @Value("#{${regions}}")
    private Map<Integer, String> regions;

    public List<String> prettyLocations(List<Integer> locs) {
        return locs.stream().map(l -> regions.get(l)).collect(Collectors.toList());
    }
}
