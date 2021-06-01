package at.fhtw.service;

import at.fhtw.client.MapQuestClient;
import at.fhtw.repository.ImageRepository;
import at.fhtw.repository.TourRepository;
import at.fhtw.service.mapper.TourMapper;
import at.fhtw.service.model.DetailedTour;
import at.fhtw.service.model.Log;
import at.fhtw.service.model.Tour;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class TourService {

    private final TourRepository tourRepository;
    private final MapQuestClient mapQuestClient;
    private final ImageRepository imageRepository;

    @Autowired
    private LogService logService;

    public List<Tour> getAllTours() {
        List<Tour> ret = new ArrayList<>();
        try {
            for (var tourEntity : tourRepository.getAllTours()) {
                var tour = TourMapper.INSTANCE.tourEntityToTour(tourEntity);
                ret.add(tour);
            }
        } catch (SQLException e) {
            log.error("Error in getting tours!", e);
        }
        return ret;
    }

    public Optional<DetailedTour> getTour(int id) {
        try {
            var tourEntity = tourRepository.getTour(id);
            if (tourEntity.isPresent()) {
                var tour = TourMapper.INSTANCE.tourEntityToDetailedTour(tourEntity.get());

                tour.setImage(getImageOfTour(tour));
//                var logs = logService.getLogsOfTour(tour.getId());

//            var routeResponse = mapQuestClient.getRoute(logToLocations(logs));
//            tour.setDistance(routeResponse.getRoute().getDistance());


                return Optional.of(tour);
            }
        } catch (SQLException e) {
            log.error("Error in getting tour {}!", id, e);
        }
        return Optional.empty();
    }

    public boolean insertOrUpdateTour(DetailedTour detailedTour) {
        try {
            if (tourRepository.tourExists(detailedTour.getId())) {
                // Update
                var optionalTourEntity = tourRepository.getTour(detailedTour.getId());
                if (optionalTourEntity.isEmpty()) {
                    return false;
                }

                var existingTour = optionalTourEntity.get();
                detailedTour.setDistance(existingTour.getDistance());
                existingTour = TourMapper.INSTANCE.combineDetailedTourWithTourEntity(existingTour, detailedTour);
                tourRepository.updateTour(existingTour);
            } else {
                // Insert
                var tourEntity = TourMapper.INSTANCE.detailedTourToTourEntity(detailedTour);
                tourRepository.insertTour(tourEntity);
            }
        } catch (SQLException e) {
            log.error("Unable to insert or update tour! ", e);
            return false;
        }
        return true;
    }

    public boolean deleteTour(int id) {
        try {
            tourRepository.deleteTour(id);
            return true;
        } catch (SQLException e) {
            log.error("Could not delete tour!", e);
        }
        return false;
    }


    @SneakyThrows
    public void asyncUpdateRouteOfTour(int tourId) {
        new Thread(() -> updateRouteOfTour(tourId)).start();
    }

    @SneakyThrows
    private void updateRouteOfTour(int tourId) {
        var key = tourId + ".jpeg";

        var logs = logService.getLogsOfTour(tourId);
        if (logs.isEmpty()) {
            return;
        }

        var locations = logToLocations(logs);
        var locationsString = String.join("||", locations);
        var route = mapQuestClient.getRoute(locations);
        var image = mapQuestClient.getMap(route.getRoute().getSessionId(), route.getRoute().getBoundingBox(), locationsString);

        imageRepository.putImage(key, image);

        var optionalTour = getTour(tourId);
        if (optionalTour.isEmpty()) {
            return;
        }

        var tour = optionalTour.get();
        tour.setDistance(route.getRoute().getDistance());

        insertOrUpdateTour(tour);
    }

    private List<String> logToLocations(List<Log> logs) {
        List<String> ret = new ArrayList<>();
        for (var log : logs) {
            var startLocation = log.getStartLocation();
            var endLocation = log.getEndLocation();

            if (ret.isEmpty()) {
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
        ret = ret.stream().filter(location -> !location.isBlank()).collect(Collectors.toList());
        log.info(ret.toString());
        return ret;
    }

    private String getImageOfTour(DetailedTour detailedTour) {
        var key = detailedTour.getId() + ".jpeg";
        if (imageRepository.hasImage(key)) {
            return Base64.getEncoder().encodeToString(imageRepository.getImage(key));
        }
        return "";
    }
}
