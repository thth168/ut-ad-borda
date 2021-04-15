package utadborda.application.services.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

public class CloudinaryResponseDTO {
    public List<Resource> resources;

    public List<String> getKeys() {
        List<String> keys = new ArrayList<>();
        for (Resource resource: resources) {
            String key = resource.secure_url;
            key = key.split("/")[8].split("\\.")[0];
            resource.key = key;
            keys.add(key);
        }
        return keys;
    }

    public Map<String, String> createEntryList() {
        Map<String, String> entries = new HashMap<>();
        for (Resource resource: resources) {
            entries.put(resource.key,resource.secure_url);
        }
        return entries;
    }

    public static class Resource {
        public long bytes;
        public String format;
        public String resource_type;
        public int width;
        public int height;
        public String secure_url;
        public String created_at;
        public String asset_id;
        public String type;
        public String version;
        public String url;
        public String public_id;
        @JsonIgnore
        public String key;
    }
}
