package at.fhtw;

import at.fhtw.client.MapQuestClient;
import at.fhtw.controller.Api;
import at.fhtw.repository.ConnectionFactory;
import at.fhtw.repository.LogRepository;
import at.fhtw.repository.TourRepository;
import at.fhtw.service.LogService;
import at.fhtw.service.TourService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class App {

    public static void main(String[] args) throws IOException, InterruptedException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        MapQuestClient mapQuestClient = new MapQuestClient();

        LogRepository logRepository = new LogRepository(connectionFactory);
        TourRepository tourRepository = new TourRepository(connectionFactory);

        LogService logService = new LogService(logRepository);
        TourService tourService = new TourService(tourRepository, logService, mapQuestClient);
        Api api = new Api(tourService);
    }
}
