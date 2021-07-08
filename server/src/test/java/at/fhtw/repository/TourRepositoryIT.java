package at.fhtw.repository;

import at.fhtw.config.EmbeddedDbExtension;
import at.fhtw.fixtures.TourFixtures;
import at.fhtw.properties.DBProperties;
import lombok.SneakyThrows;
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
class TourRepositoryIT {

    @Autowired
    private DBProperties dbProperties;

    private TourRepository tourRepository;

    @SneakyThrows
    @Test
    void getAllTours() {
        assertThat(tourRepository.getAllTours()).isEmpty();

        var carTour = TourFixtures.carTourEntity();
        tourRepository.insertTourWithId(carTour);
        assertThat(tourRepository.getAllTours()).contains(carTour);
    }

    @SneakyThrows
    @Test
    void getTour() {
        var carTour = TourFixtures.carTourEntity();
        tourRepository.insertTourWithId(carTour);
        assertThat(tourRepository.getTour(carTour.getId())).contains(carTour);
    }

    @SneakyThrows
    @Test
    void tourExists() {
        var carTour = TourFixtures.carTourEntity();
        assertThat(tourRepository.tourExists(carTour.getId())).isFalse();
        tourRepository.insertTourWithId(carTour);
        assertThat(tourRepository.tourExists(carTour.getId())).isTrue();
    }

    @SneakyThrows
    @Test
    void deleteTour() {
        var carTour = TourFixtures.carTourEntity();
        tourRepository.insertTourWithId(carTour);
        assertThat(tourRepository.tourExists(carTour.getId())).isTrue();
        tourRepository.deleteTour(carTour.getId());
        assertThat(tourRepository.tourExists(carTour.getId())).isFalse();
    }

    @SneakyThrows
    @Test
    void updateTour() {
        var carTour = TourFixtures.carTourEntity();
        tourRepository.insertTourWithId(carTour);
        assertThat(tourRepository.getTour(carTour.getId()).get().getName()).isEqualTo(carTour.getName());

        carTour.setName("New name");
        tourRepository.updateTour(carTour);
        assertThat(tourRepository.getTour(carTour.getId()).get().getName()).isEqualTo(carTour.getName());
    }

    @SneakyThrows
    @Test
    void insertTour() {
        var carTour = TourFixtures.carTourEntity();
        tourRepository.insertTour(carTour);
        assertThat(tourRepository.tourExists(carTour.getId())).isTrue();
    }

    @SneakyThrows
    @Test
    void insertTourWithId() {
        var carTour = TourFixtures.carTourEntity();
        tourRepository.insertTourWithId(carTour);
        assertThat(tourRepository.tourExists(carTour.getId())).isTrue();
    }

    @BeforeEach
    private void setUp() {
        tourRepository = new TourRepositoryImpl(new ConnectionFactory(dbProperties));
    }

    @SneakyThrows
    @AfterEach
    private void tearDown() {
        tourRepository.deleteAll();
    }
}
