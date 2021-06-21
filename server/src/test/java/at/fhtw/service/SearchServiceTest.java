package at.fhtw.service;

import at.fhtw.fixtures.LogFixtures;
import at.fhtw.fixtures.TourFixtures;
import at.fhtw.repository.LogRepository;
import at.fhtw.repository.TourRepository;
import at.fhtw.service.model.SearchRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class SearchServiceTest {

    private LogRepository logRepository;
    private TourRepository tourRepository;
    private ObjectMapper objectMapper;
    private SearchService searchService;

    @BeforeEach
    void setUp() {
        logRepository = Mockito.mock(LogRepository.class);
        tourRepository = Mockito.mock(TourRepository.class);
        objectMapper = new ObjectMapper();

        searchService = new SearchService(logRepository, tourRepository, objectMapper);
    }

    @SneakyThrows
    @Test
    void search() {
        var carLog = LogFixtures.carLogEntity();
        var bikeLog = LogFixtures.bikeLogEntity();
        when(logRepository.getAllLogs()).thenReturn(List.of(carLog, bikeLog));
        when(logRepository.getLog(carLog.getId())).thenReturn(Optional.of(carLog));
        when(logRepository.getLog(bikeLog.getId())).thenReturn(Optional.of(bikeLog));

        var carTour = TourFixtures.carTourEntity();
        var bikeTour = TourFixtures.bikeTourEntity();
        when(tourRepository.getAllTours()).thenReturn(List.of(carTour, bikeTour));

        assertThat(searchService
                .search(SearchRequest.builder().searchString(carLog.getNotes()).build())
                .getLogIDs()
        )
                .contains(Integer.toString(carLog.getId()))
                .contains(Integer.toString(bikeLog.getId()))
                .hasSize(2);

        assertThat(searchService
                .search(SearchRequest.builder().searchString(carLog.getMeansOfTransport()).build())
                .getLogIDs()
        )
                .contains(Integer.toString(carLog.getId()))
                .hasSize(1);
    }
}
