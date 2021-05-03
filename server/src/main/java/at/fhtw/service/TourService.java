package at.fhtw.service;

import at.fhtw.client.MapQuestClient;
import at.fhtw.repository.TourRepository;
import at.fhtw.service.mapper.TourMapper;
import at.fhtw.service.model.DetailedTour;
import at.fhtw.service.model.Log;
import at.fhtw.service.model.Tour;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class TourService {

    private final TourRepository tourRepository;
    private final LogService logService;
    private final MapQuestClient mapQuestClient;

    public List<Tour> getAllTours() {
        List<Tour> ret = new ArrayList<>();
        for (var tourEntity : tourRepository.getAllTours()) {
            var tour = TourMapper.INSTANCE.tourEntityToTour(tourEntity);
            ret.add(tour);
        }
        return ret;
    }

    public Optional<DetailedTour> getTour(int id) throws IOException {
        var tourEntity = tourRepository.getTour(id);
        if (tourEntity.isPresent()) {
            var tour = TourMapper.INSTANCE.tourEntityToDetailedTour(tourEntity.get());

            var logs = logService.getLogsOfTour(tour.getId());
            var routeResponse = mapQuestClient.getRoute(logToLocations(logs));

            tour.setDistance(routeResponse.getRoute().getDistance());

            return Optional.of(tour);
        }
        return Optional.empty();
    }

    private List<String> logToLocations(List<Log> logs) {
        List<String> ret = new ArrayList<>();
        for (var log : logs) {
            var startLocation = log.getStartLocation();
            var endLocation = log.getEndLocation();

            if (ret.size() == 0) {
                ret.add(startLocation);
                ret.add(endLocation);
            } else {
                if (!ret.get(ret.size() - 1).equalsIgnoreCase(startLocation)) {
                    ret.add(startLocation);
                }
                if (!ret.get(ret.size() - 1).equalsIgnoreCase(endLocation)) {
                    ret.add(endLocation);
                }
            }
        }
        return ret;
    }
}
