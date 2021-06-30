package at.fhtw.controller;

import at.fhtw.service.interfaces.ReportService;
import at.fhtw.service.interfaces.TourService;
import at.fhtw.service.model.DetailedTour;
import at.fhtw.service.model.Tour;
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
public class ToursController {
    private final ReportService reportService;
    private final TourService tourService;

    @GetMapping(path = "/tours/{id}/report", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getTourReport(@PathVariable int id) {
        var content = reportService.generateReportForTour(id);

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        headers.add("Content-Disposition", "inline; filename=" + "output.pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }

    @GetMapping(path = "/tours", produces = "application/json")
    public List<Tour> getTours() {
        return tourService.getAllTours();
    }

    @PutMapping(path = "/tours")
    public void putTour(@RequestBody DetailedTour detailedTour) {
        if (tourService.insertOrUpdateTour(detailedTour)) {
            return;
        }
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Unable to update tour"
        );
    }

    @DeleteMapping(path = "/tours/{id}")
    public void deleteTour(@PathVariable int id) {
        tourService.deleteTour(id);
    }

    @GetMapping(path = "/tours/{id}", produces = "application/json")
    private DetailedTour getTour(@PathVariable int id) {
        var optionalTour = tourService.getTour(id);
        if (optionalTour.isPresent()) {
            return optionalTour.get();
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Tour not found"
        );
    }
}
