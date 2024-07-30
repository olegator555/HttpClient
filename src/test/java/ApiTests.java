import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.CrptApi;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ApiTests {

    private String messageBody = Files.readString(
            Paths.get(this.getClass().getResource("messageBody.json").toURI()), Charset.defaultCharset());

    private final CrptApi crptApi = new CrptApi(TimeUnit.SECONDS, 5);

    public ApiTests() throws IOException, URISyntaxException {
    }

    @Test
    @SuppressWarnings("unchecked")
    public void apiTest() throws IOException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        Map<String, String> requestBody = mapper.readValue(messageBody, Map.class);
        crptApi.accessApi(requestBody);
        System.out.println();

    }
}
