package at.fhtw.repository;

import at.fhtw.config.EmbeddedDbExtension;
import at.fhtw.fixtures.LogFixtures;
import at.fhtw.fixtures.TourFixtures;
import at.fhtw.properties.DBProperties;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({
        EmbeddedDbExtension.class
})
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
@Slf4j
class LogRepositoryIT {

    @Autowired
    private DBProperties dbProperties;

    private LogRepository logRepository;
    private TourRepository tourRepository;

    @SneakyThrows
    @Test
    void getAllLogs() {
        assertThat(logRepository.getAllLogs())
                .contains(LogFixtures.bikeLogEntity())
                .contains(LogFixtures.carLogEntity())
                .hasSize(2);
    }

    @Test
    void getLog() {
        assertThat(logRepository.getLog(LogFixtures.bikeLogEntity().getId()))
                .contains(LogFixtures.bikeLogEntity());

        assertThat(logRepository.getLog(123))
                .isEmpty();
    }

    @SneakyThrows
    @Test
    void logExists() {
        assertThat(logRepository.logExists(LogFixtures.bikeLogEntity().getId()))
                .isTrue();

        assertThat(logRepository.logExists(123))
                .isFalse();
    }

    @Test
    void getLogsOfTour() {
        assertThat(logRepository.getLogsOfTour(LogFixtures.bikeLogEntity().getTourId()))
                .contains(LogFixtures.bikeLogEntity())
                .hasSize(1);
    }

    @Test
    void getDistanceOfTour() {
        assertThat(logRepository.getDistanceOfTour(LogFixtures.bikeLogEntity().getTourId()))
                .isEqualTo(LogFixtures.bikeLogEntity().getDistance());

        assertThat(logRepository.getDistanceOfTour(123))
                .isZero();
    }

    @SneakyThrows
    @Test
    void updateLog() {
        var bikeLog = LogFixtures.bikeLogEntity();
        assertThat(logRepository.getLog(bikeLog.getId()))
                .contains(bikeLog);

        bikeLog.setNotes("These notes are great!");
        logRepository.updateLog(bikeLog);
        assertThat(logRepository.getLog(bikeLog.getId()))
                .contains(bikeLog);
    }

    @SneakyThrows
    @Test
    void deleteLog() {
        var bikeLog = LogFixtures.bikeLogEntity();
        assertThat(logRepository.getLog(bikeLog.getId()))
                .contains(bikeLog);
        logRepository.deleteLog(bikeLog.getId());
        assertThat(logRepository.getLog(bikeLog.getId()))
                .isEmpty();
    }

    @BeforeEach
    @SneakyThrows
    private void setUp() {
        log.info("BeforeEach");
        logRepository = new LogRepository(new ConnectionFactory(dbProperties));
        tourRepository = new TourRepository(new ConnectionFactory(dbProperties));

        var carTour = TourFixtures.carTourEntity();
        var bikeTour = TourFixtures.bikeTourEntity();
        tourRepository.insertTourWithId(carTour);
        tourRepository.insertTourWithId(bikeTour);

        var carLog = LogFixtures.carLogEntity();
        var bikeLog = LogFixtures.bikeLogEntity();
        logRepository.insertLogWithId(carLog);
        logRepository.insertLogWithId(bikeLog);
    }

    @SneakyThrows
    @AfterEach
    private void tearDown() {
        log.info("AfterEach");
        tourRepository.deleteAll();
        logRepository.deleteAll();
    }
}
