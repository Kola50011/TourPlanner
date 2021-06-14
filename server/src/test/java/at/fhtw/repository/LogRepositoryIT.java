package at.fhtw.repository;

import at.fhtw.config.EmbeddedDbExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith({
        EmbeddedDbExtension.class
})
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
class LogRepositoryIT {

    @Test
    void getAllLogs() {
    }

    @Test
    void getLog() {
    }

    @Test
    void logExists() {
    }

    @Test
    void getLogsOfTour() {
    }

    @Test
    void getDistanceOfTour() {
    }

    @Test
    void updateLog() {
    }

    @Test
    void insertTour() {
    }

    @Test
    void deleteLog() {
    }
}
