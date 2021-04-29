package at.fhtw.controller;


import at.fhtw.service.TourService;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Api {

    public Api(TourService tourService) throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        ToursController toursController = new ToursController(httpServer, tourService);

        httpServer.start();
    }
}
