package utadborda.application.services.DTO;

import java.util.List;

public class RestTagCategoryDTO {
    private final List<String> categories;

    public RestTagCategoryDTO(List<String> categories) {
        this.categories = categories;
    }

    public List<String> getCategories() {
        return this.categories;
    }
}
