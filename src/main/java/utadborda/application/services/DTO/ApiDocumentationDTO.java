package utadborda.application.services.DTO;

import java.util.List;

public class ApiDocumentationDTO {
    public List<Endpoint> endpoints;

    public static class Endpoint {
        public String path;
        public String description;
        public List<QueryParam> queryParams;
    }

    public static class QueryParam {
        public String query;
        public String type;
        public Object defaultValue;
        public boolean required;
        public String description;
    }
}
