package at.fhtw;

import at.fhtw.controller.Api;
import at.fhtw.repository.ConnectionFactory;
import at.fhtw.repository.TourRepository;
import at.fhtw.service.TourService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {

    @SneakyThrows
    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        TourRepository tourRepository = new TourRepository(connectionFactory);
        TourService tourService = new TourService(tourRepository);
        Api api = new Api(tourService);

//        var tours = TourRepository.getInstance().getAllTours();
//        log.info(TourRepository.getInstance().getTour(tours.get(0).getId()).get().toString());
//        log.info(tours.toString());
    }
}
