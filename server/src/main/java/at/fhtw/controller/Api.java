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

    private ToursController toursController;
    private LogsController logsController;

    public Api(LogService logService, TourService tourService) throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        logsController = new LogsController(logService);
        toursController = new ToursController(tourService);
        httpServer.createContext("/", this::handleToursRequest);
        httpServer.start();
    }

    private void sendResponse(HttpExchange exchange, String body) throws IOException {
        byte[] responseBody = body.getBytes();
        exchange.sendResponseHeaders(200, responseBody.length);
        OutputStream os = exchange.getResponseBody();
        os.write(responseBody);
        os.close();
    }

    private void handleToursRequest(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            List<String> subPaths = new ArrayList<>(Arrays.asList(path.split("/")));

            subPaths.removeAll(Arrays.asList("", null));

            var requestMethod = exchange.getRequestMethod();
            log.info("{} called with path {}", requestMethod, path);

            if (requestMethod.equalsIgnoreCase("get")) {
                if (subPaths.size() == 1) {
                    String firstSubPath = subPaths.get(0);
                    if (firstSubPath.equals("tours")) {
                        var tours = toursController.getTours();
                        exchange.getResponseHeaders().add("Content-Type", "application/json");
                        sendResponse(exchange, tours);
                        return;
                    }
                } else if (subPaths.size() == 2) {
                    String firstSubPath = subPaths.get(0);
                    var id = Integer.parseInt(subPaths.get(1));
                    Optional<String> response = Optional.empty();
                    switch (firstSubPath) {
                        case "tours":
                            response = toursController.getTour(id);
                            break;
                        case "logs":
                            response = logsController.getLogs(id);
                            break;
                        default:
                            break;
                    }

                    if (response.isPresent()) {
                        exchange.getResponseHeaders().add("Content-Type", "application/json");
                        sendResponse(exchange, response.get());
                        return;
                    }
                }
            } else if (requestMethod.equalsIgnoreCase("put")) {
                if (subPaths.size() == 1) {
                    var body = new String(exchange.getRequestBody().readAllBytes());

                    var returnCode = toursController.putTour(body);
                    exchange.sendResponseHeaders(returnCode, 0);
                }
            } else if (requestMethod.equalsIgnoreCase("delete")) {
                if (subPaths.size() == 2) {
                    String firstSubPath = subPaths.get(0);
                    var id = Integer.parseInt(subPaths.get(1));
                    switch (firstSubPath) {
                        case "tours":
                            toursController.deleteTour(id);
                            exchange.sendResponseHeaders(200, 0);
                            break;
                        default:
                            break;
                    }
                }
            }

            exchange.sendResponseHeaders(404, 0);
            exchange.close();
        } catch (IOException e) {
            exchange.sendResponseHeaders(400, 0);
            exchange.close();
        }
    }
}
