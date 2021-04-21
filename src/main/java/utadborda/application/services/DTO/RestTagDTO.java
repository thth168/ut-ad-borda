package utadborda.application.services.DTO;

import utadborda.application.Entities.Tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestTagDTO {
    private final List<Tag> tags;
    private final String category;

    public RestTagDTO(List<Tag> tags) {
        this.tags = tags;
        this.category = "all";
    }

    public RestTagDTO(String category, List<Tag> tags) {
        this.tags = tags;
        this.category = category;
    }

    public String getCategory() { return category; }
    public List<Tag> getFilters() {
        return tags;
    }
}
