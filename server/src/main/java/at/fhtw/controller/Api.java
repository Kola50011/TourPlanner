package at.fhtw.controller;


import at.fhtw.service.TourService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Api {

    private ToursController toursController;

    public Api(TourService tourService) throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
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
        String path = exchange.getRequestURI().getPath();
        List<String> subPaths = new ArrayList<>(Arrays.asList(path.split("/")));

        subPaths.removeAll(Arrays.asList("", null));

        if (subPaths.size() == 1) {
            String firstSubPath = subPaths.get(0);
            if (firstSubPath.equals("tours")) {
                var tours = toursController.getTours();
                sendResponse(exchange, tours);
                return;
            }
        } else if (subPaths.size() == 2) {
            String firstSubPath = subPaths.get(0);
            if (firstSubPath.equals("tours")) {
                var tour = toursController.getTour(Integer.parseInt(subPaths.get(1)));
                sendResponse(exchange, tour);
                return;
            }
        }
        exchange.sendResponseHeaders(404, 0);
    }
}
