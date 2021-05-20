package at.fhtw.controller;

import at.fhtw.service.LogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
public class LogsController {

    private final LogService logService;
    private ObjectMapper objectMapper = new ObjectMapper();

    public Optional<String> getLogs(int tour) throws IOException {
        return Optional.of(objectMapper.writeValueAsString(logService.getLogsOfTour(tour)));
    }
}
