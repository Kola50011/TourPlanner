package at.fhtw.properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@Slf4j
public class PropertyReader {

    public static ApplicationProperties getProperties() {
        try {
            var mapper = new ObjectMapper(new YAMLFactory());
            return mapper.readValue(new File("src/main/resources/application.yml"),
                    ApplicationProperties.class);
        } catch (IOException e) {
            log.error("Unable to read application properties!", e);
        }
        return new ApplicationProperties();
    }
}
