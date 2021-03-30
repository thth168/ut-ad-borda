package utadborda.application.services.DTO;

import utadborda.application.Entities.Tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestFilterDTO {
    private final Map<String, List<Tag>> filters;
    public RestFilterDTO(List<String> categories, List<List<Tag>> tags) {
        this.filters = new HashMap<>();
        for (int i = 0; i < categories.size() && i < tags.size(); i++) {
            this.filters.put(categories.get(i), tags.get(i));
        }
    }

    public Map<String, List<Tag>> getFilters() {
        return filters;
    }
}
