package at.fhtw.controller;

import at.fhtw.service.LogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


@Slf4j
@RequiredArgsConstructor
public class LogsController {

    private final LogService logService;
    private ObjectMapper objectMapper = new ObjectMapper();

    public String getLogs(int tour) throws IOException {
        return objectMapper.writeValueAsString(logService.getLogsOfTour(tour));
    }
}
