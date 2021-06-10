package at.fhtw;

import at.fhtw.properties.DBProperties;
import at.fhtw.properties.MapQuestProperties;
import at.fhtw.properties.MinioProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({MinioProperties.class, DBProperties.class, MapQuestProperties.class})
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
