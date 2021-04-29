package at.fhtw.controller;

import at.fhtw.service.TourService;
import at.fhtw.service.model.Tour;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.OutputStream;
import java.net.URI;
import java.util.List;


@Slf4j
public class ToursController {

    private final HttpServer httpServer;
    private final TourService tourService;
    private ObjectMapper objectMapper = new ObjectMapper();

    public ToursController(HttpServer httpServer, TourService tourService) {
        this.httpServer = httpServer;
        this.tourService = tourService;

        httpServer.createContext("/tours", this::handleToursRequest);
    }

    @SneakyThrows
    private void handleToursRequest(HttpExchange exchange) {
        URI requestURI = exchange.getRequestURI();

        if (exchange.getRequestMethod().equals("GET")) {
            var tours = getTours();

            try {
                objectMapper.writeValueAsString(tours);
            } catch (Exception e) {
                log.error("asd", e);
            }
            String response = objectMapper.writeValueAsString(tours);
            exchange.sendResponseHeaders(200, response.getBytes().length);

            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private List<Tour> getTours() {
        return tourService.getAllTours();
    }
}
