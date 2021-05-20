package at.fhtw.service;

import at.fhtw.client.MapQuestClient;
import at.fhtw.repository.ImageRepository;
import at.fhtw.repository.TourRepository;
import at.fhtw.service.mapper.TourMapper;
import at.fhtw.service.model.DetailedTour;
import at.fhtw.service.model.Log;
import at.fhtw.service.model.Tour;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class TourService {

    private final TourRepository tourRepository;
    private final LogService logService;
    private final MapQuestClient mapQuestClient;
    private final ImageRepository imageRepository;

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
        } catch (SQLException | IOException e) {
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
        log.info(ret.toString());
        return ret;
    }

    private String getImageOfTour(DetailedTour detailedTour) throws IOException {
        var key = detailedTour.getId() + ".jpeg";
        if (imageRepository.hasImage(key)) {
            return Base64.getEncoder().encodeToString(imageRepository.getImage(key));
        }

        var logs = logService.getLogsOfTour(detailedTour.getId());
        if (logs.size() == 0) {
            return "";
        }

        var locations = logToLocations(logs);
        var locationsString = String.join("||", locations);
        var route = mapQuestClient.getRoute(locations);
        var image = mapQuestClient.getMap(route.getRoute().getSessionId(), route.getRoute().getBoundingBox(), locationsString);

        imageRepository.putImage(key, image);
        return Base64.getEncoder().encodeToString(image);
    }
}
