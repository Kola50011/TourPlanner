package at.fhtw.controller;

import at.fhtw.service.LogService;
import at.fhtw.service.model.Log;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Optional;


@RequiredArgsConstructor
@Slf4j
public class LogsController {

    private final LogService logService;
    private ObjectMapper objectMapper = new ObjectMapper();

    public Optional<String> getLogsOfTour(int tour) throws IOException {
        return Optional.of(objectMapper.writeValueAsString(logService.getLogsOfTour(tour)));
    }

    public Optional<String> getLog(int logId) throws IOException {
        var optionalLog = logService.getLog(logId);
        if (optionalLog.isPresent()) {
            return Optional.of(objectMapper.writeValueAsString(optionalLog.get()));
        }
        return Optional.empty();
    }

    public int putLog(String body) throws IOException {
        var tourLog = objectMapper.readValue(body, Log.class);
        log.info("{}", log);
        if (logService.insertOrUpdateLog(tourLog)) {
            return 200;
        }
        return 500;
    }

    public void deleteLog(int id) {
        logService.deleteLog(id);
    }
}
