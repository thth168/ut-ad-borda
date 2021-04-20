package utadborda.application.RestControllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import utadborda.application.services.DTO.ApiDocumentationDTO;
import utadborda.application.web.requestMappings;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@RestController
public class HomeRestController {

    private ObjectMapper objectMapper;

    @Autowired
    public HomeRestController(
        ObjectMapper objectMapper
    ) {
        this.objectMapper = objectMapper;
    }
    @GetMapping(requestMappings.API)
    public ApiDocumentationDTO apiDocumentation() throws IOException {
        String jsonString = loadJsonAsString("src/main/resources/static/json/apiDocumentation.json");
        return objectMapper.readValue(jsonString, ApiDocumentationDTO.class);
    }

    private String loadJsonAsString(String filename) throws IOException {
        StringBuilder builder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(filename), StandardCharsets.UTF_8)) {
            stream.forEach(s -> builder.append(s).append('\n'));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.toString();
    }
}
