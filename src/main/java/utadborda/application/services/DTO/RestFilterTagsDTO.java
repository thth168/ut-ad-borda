package utadborda.application.services.DTO;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class RestFilterTagsDTO {
    private List<String> tags;

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
