package at.fhtw.controller;

import at.fhtw.service.interfaces.LogService;
import at.fhtw.service.interfaces.ReportService;
import at.fhtw.service.model.Log;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LogsController {
    private final LogService logService;
    private final ReportService reportService;

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

    @GetMapping(path = "/logs", produces = "application/json")
    public List<Log> getLogs() {
        return logService.getLogs();
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

    @GetMapping(path = "/logs/report", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getLogsReport() {
        var content = reportService.generateLogsReport();

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        headers.add("Content-Disposition", "inline; filename=" + "output.pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }

    @GetMapping(path = "/logs/htmlReport", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<String> getLogsReportHtml() {
        var content = reportService.generateLogsReportHtml();

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);

        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }
}
