package at.fhtw.controller;

import at.fhtw.service.TourService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


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

    public String getTour(int tour) throws IOException {
        var optinalTour = tourService.getTour(tour);
        if (optinalTour.isPresent()) {
            return objectMapper.writeValueAsString(optinalTour.get());
        }
        return "{}";
    }
}
