package at.fhtw.controller;

import at.fhtw.service.TourService;
import at.fhtw.service.model.DetailedTour;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Optional;


@Slf4j
public class ToursController {

    private final TourService tourService;
    private ObjectMapper objectMapper = new ObjectMapper();

    public ToursController(TourService tourService) {
        this.tourService = tourService;
    }

    public String getTours() throws JsonProcessingException {
        return objectMapper.writeValueAsString(tourService.getAllTours());
    }

    public Optional<String> getTour(int tour) throws IOException {
        var optinalTour = tourService.getTour(tour);
        if (optinalTour.isPresent()) {
            return Optional.of(objectMapper.writeValueAsString(optinalTour.get()));
        }
        return Optional.empty();
    }

    public int putTour(String body) throws IOException {
        var detailedTour = objectMapper.readValue(body, DetailedTour.class);
        log.info("{}", detailedTour);
        if (tourService.insertOrUpdateTour(detailedTour)) {
            return 200;
        }
        return 500;
    }

    public void deleteTour(int id) {
        tourService.deleteTour(id);
    }
}
