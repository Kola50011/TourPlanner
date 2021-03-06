package at.fhtw.service;

import at.fhtw.fixtures.TourFixtures;
import at.fhtw.repository.ImageRepositoryImpl;
import at.fhtw.repository.TourRepositoryImpl;
import at.fhtw.service.impl.LogServiceImpl;
import at.fhtw.service.impl.TourServiceImpl;
import at.fhtw.service.interfaces.TourService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TourServiceTest {

    private TourService tourService;

    private TourRepositoryImpl tourRepository;

    @Test
    void getAllTours() throws SQLException {
        when(tourRepository.getAllTours()).thenReturn(List.of(
                TourFixtures.carTourEntity(),
                TourFixtures.bikeTourEntity()));

        var tours = tourService.getAllTours();
        assertThat(tours).contains(TourFixtures.carTour())
                .contains(TourFixtures.bikeTour());
    }

    @Test
    void getAllDetailedTours() throws SQLException {
        when(tourRepository.getAllTours()).thenReturn(List.of(
                TourFixtures.carTourEntity(),
                TourFixtures.bikeTourEntity()));

        var tours = tourService.getAllDetailedTours();
        assertThat(tours).contains(TourFixtures.detailedCarTour())
                .contains(TourFixtures.detailedBikeTour());
    }

    @Test
    void getTour() throws SQLException {
        when(tourRepository.getTour(1)).thenReturn(Optional.of(TourFixtures.carTourEntity()));
        when(tourRepository.getTour(3)).thenReturn(Optional.empty());

        var carTour = TourFixtures.detailedCarTour();
        carTour.setImage("");
        assertThat(tourService.getTour(1)).isEqualTo(Optional.of(carTour));
        assertThat(tourService.getTour(3)).isNotPresent();
    }

    @Test
    void insertOrUpdateTour() throws SQLException {
        when(tourRepository.tourExists(1)).thenReturn(true);
        when(tourRepository.getTour(1)).thenReturn(Optional.of(TourFixtures.carTourEntity()));
        when(tourRepository.tourExists(3)).thenReturn(false);
        when(tourRepository.getTour(3)).thenReturn(Optional.empty());

        tourService.insertOrUpdateTour(TourFixtures.detailedCarTour());
        verify(tourRepository).updateTour(any());

        tourService.insertOrUpdateTour(TourFixtures.detailedBikeTour());
        verify(tourRepository).insertTourWithId(any());
    }

    @Test
    void deleteTour() throws SQLException {
        when(tourRepository.getTour(1)).thenReturn(Optional.of(TourFixtures.carTourEntity()));
        doNothing().when(tourRepository).deleteTour(1);
        doThrow(new SQLException("lol")).when(tourRepository).deleteTour(3);

        assertThat(tourService.deleteTour(1)).isTrue();
        assertThat(tourService.deleteTour(3)).isFalse();
    }

    @BeforeEach
    private void setUp() {
        tourRepository = Mockito.mock(TourRepositoryImpl.class);
        ImageRepositoryImpl imageRepository = Mockito.mock(ImageRepositoryImpl.class);
        LogServiceImpl logService = Mockito.mock(LogServiceImpl.class);

        tourService = new TourServiceImpl(tourRepository, imageRepository, logService);
    }
}
