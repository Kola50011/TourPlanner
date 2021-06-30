package at.fhtw.service;

import at.fhtw.client.MapQuestClient;
import at.fhtw.client.model.Info;
import at.fhtw.client.model.Route;
import at.fhtw.client.model.RouteResponse;
import at.fhtw.fixtures.LogFixtures;
import at.fhtw.repository.ImageRepository;
import at.fhtw.repository.LogRepository;
import at.fhtw.service.impl.LogServiceImpl;
import at.fhtw.service.interfaces.LogService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LogServiceTest {

    private LogService logService;
    private LogRepository logRepository;
    private MapQuestClient mapQuestClient;

    @BeforeEach
    void setUp() {
        ImageRepository imageRepository = Mockito.mock(ImageRepository.class);
        logRepository = Mockito.mock(LogRepository.class);
        mapQuestClient = Mockito.mock(MapQuestClient.class);

        logService = new LogServiceImpl(logRepository, mapQuestClient, imageRepository);
    }

    @Test
    void getLogs() {
        when(logRepository.getAllLogs()).thenReturn(List.of(LogFixtures.bikeLogEntity(), LogFixtures.carLogEntity()));

        var logs = logService.getLogs();
        assertThat(logs).contains(LogFixtures.bikeLog()).contains(LogFixtures.carLog());
    }

    @Test
    void getLogsOfTour() {
        when(logRepository.getLogsOfTour(1)).thenReturn(List.of(LogFixtures.bikeLogEntity()));
        assertThat(logService.getLogsOfTour(1)).contains(LogFixtures.bikeLog()).hasSize(1);
        assertThat(logService.getLogsOfTour(2)).isEmpty();
    }

    @Test
    void getDistanceOfTour() {
        when(logRepository.getDistanceOfTour(1)).thenReturn(5.0f);
        assertThat(logService.getDistanceOfTour(1)).isEqualTo(5.0f);
    }

    @Test
    void getLog() {
        when(logRepository.getLog(1)).thenReturn(Optional.of(LogFixtures.bikeLogEntity()));
        when(logRepository.getLog(2)).thenReturn(Optional.empty());
        assertThat(logService.getLog(1)).contains(LogFixtures.bikeLog());
        assertThat(logService.getLog(2)).isEmpty();
    }

    @SneakyThrows
    @Test
    void insertOrUpdateLog() {
        when(logRepository.logExists(1)).thenReturn(true);
        when(logRepository.getLog(1)).thenReturn(Optional.of(LogFixtures.carLogEntity()));
        when(logRepository.logExists(2)).thenReturn(false);
        when(logRepository.getLog(2)).thenReturn(Optional.empty());

        when(mapQuestClient.getRoute(any())).thenReturn(
                RouteResponse.builder()
                        .info(Info.builder().statuscode(0).build())
                        .route(Route.builder().distance(5.0f).build())
                        .build()
        );

        logService.insertOrUpdateLog(LogFixtures.carLog());
        verify(logRepository).updateLog(any());

        logService.insertOrUpdateLog(LogFixtures.bikeLog());
        verify(logRepository).insertLog(any());
    }

    @SneakyThrows
    @Test
    void deleteLog() {
        doNothing().when(logRepository).deleteLog(1);
        when(logRepository.getLog(1)).thenReturn(Optional.of(LogFixtures.carLogEntity()));
        doThrow(new SQLException("lol")).when(logRepository).deleteLog(3);
        when(logRepository.getLog(3)).thenReturn(Optional.of(LogFixtures.bikeLogEntity()));

        assertThat(logService.deleteLog(1)).isTrue();
        assertThat(logService.deleteLog(3)).isFalse();
    }
}
