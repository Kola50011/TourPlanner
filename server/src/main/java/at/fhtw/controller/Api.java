package at.fhtw.controller;


import at.fhtw.service.LogService;
import at.fhtw.service.TourService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
public class Api {

    private static final String TOURS_PATH = "tours";
    private static final String LOGS_PATH = "logs";
    private static final String UPDATE_TOUR_PATH = "update";

    private final ToursController toursController;
    private final LogsController logsController;

    public Api(LogService logService, TourService tourService) throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        logsController = new LogsController(logService);
        toursController = new ToursController(tourService);
        httpServer.createContext("/", this::handleRequest);
        httpServer.start();
    }

    private void sendResponse(HttpExchange exchange, String body) throws IOException {
        byte[] responseBody = body.getBytes();
        exchange.sendResponseHeaders(200, responseBody.length);
        OutputStream os = exchange.getResponseBody();
        os.write(responseBody);
        os.close();
    }

    private void handleGet(HttpExchange exchange, List<String> subPaths) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        Optional<String> response = Optional.empty();

        if (subPaths.size() == 1) {
            String firstSubPath = subPaths.get(0);
            if (firstSubPath.equals(TOURS_PATH)) {
                var tours = toursController.getTours();
                response = Optional.of(tours);
            }
        } else if (subPaths.size() == 2) {
            String firstSubPath = subPaths.get(0);
            var id = Integer.parseInt(subPaths.get(1));
            switch (firstSubPath) {
                case TOURS_PATH:
                    response = toursController.getTour(id);
                    break;
                case LOGS_PATH:
                    response = logsController.getLog(id);
                    break;
                default:
                    break;
            }


        } else if (subPaths.size() == 3) {
            String firstSubPath = subPaths.get(0);
            var id = Integer.parseInt(subPaths.get(1));
            String thirdSubPath = subPaths.get(2);
            if (firstSubPath.equalsIgnoreCase(TOURS_PATH) && thirdSubPath.equalsIgnoreCase(LOGS_PATH)) {
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                response = logsController.getLogsOfTour(id);
            }
        }

        if (response.isPresent()) {
            sendResponse(exchange, response.get());
        }
    }

    private void handlePut(HttpExchange exchange, List<String> subPaths) throws IOException {
        var body = new String(exchange.getRequestBody().readAllBytes());

        if (subPaths.size() == 1) {
            String firstSubPath = subPaths.get(0);
            switch (firstSubPath) {
                case TOURS_PATH:
                    exchange.sendResponseHeaders(toursController.putTour(body), 0);
                    break;
                case LOGS_PATH:
                    exchange.sendResponseHeaders(logsController.putLog(body), 0);
                    break;
                default:
                    break;
            }
        }
    }

    private void handleDelete(HttpExchange exchange, List<String> subPaths) throws IOException {
        if (subPaths.size() == 2) {
            String firstSubPath = subPaths.get(0);
            var id = Integer.parseInt(subPaths.get(1));
            switch (firstSubPath) {
                case TOURS_PATH:
                    toursController.deleteTour(id);
                    exchange.sendResponseHeaders(200, 0);
                    break;
                case LOGS_PATH:
                    logsController.deleteLog(id);
                    exchange.sendResponseHeaders(200, 0);
                    break;
                default:
                    break;
            }
        }
    }


    private void handleRequest(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            List<String> subPaths = new ArrayList<>(Arrays.asList(path.split("/")));

            subPaths.removeAll(Arrays.asList("", null));

            var requestMethod = exchange.getRequestMethod();
            log.info("Request: {} {}", requestMethod, path);

            if (requestMethod.equalsIgnoreCase("get")) {
                handleGet(exchange, subPaths);
            } else if (requestMethod.equalsIgnoreCase("put")) {
                handlePut(exchange, subPaths);
            } else if (requestMethod.equalsIgnoreCase("delete")) {
                handleDelete(exchange, subPaths);
            }

            if (exchange.getResponseCode() == -1) {
                exchange.sendResponseHeaders(404, 0);
            }
            log.info("Response: {}", exchange.getResponseCode());

            exchange.close();
        } catch (IOException e) {
            exchange.sendResponseHeaders(400, 0);
            exchange.close();
        }
    }
}
