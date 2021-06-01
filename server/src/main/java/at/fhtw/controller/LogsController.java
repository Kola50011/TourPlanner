package at.fhtw.controller;

import at.fhtw.service.LogService;
import at.fhtw.service.model.Log;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LogsController {
    private final LogService logService;

    @GetMapping(path = "/logs/{id}", produces = "application/json")
    public Log getLog(@PathVariable int id) {
        var optionalLog = logService.getLog(id);
        if (optionalLog.isPresent()) {
            return optionalLog.get();
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Log not found"
        );
    }

    @DeleteMapping(path = "/logs/{id}")
    public void deleteLog(@PathVariable int id) {
        logService.deleteLog(id);
    }

    @GetMapping(path = "/tours/{tourId}/logs", produces = "application/json")
    public List<Log> getLogsOfTour(@PathVariable int tourId) {
        return logService.getLogsOfTour(tourId);
    }

    @PutMapping(path = "/logs")
    public void putLog(@RequestBody Log tourLog) {
        if (logService.insertOrUpdateLog(tourLog)) {
            return;
        }
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Unable to update log"
        );
    }
}
