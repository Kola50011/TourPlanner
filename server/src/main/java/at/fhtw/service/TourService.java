package at.fhtw.service;

import at.fhtw.repository.ImageRepository;
import at.fhtw.repository.TourRepository;
import at.fhtw.service.mapper.TourMapper;
import at.fhtw.service.model.DetailedTour;
import at.fhtw.service.model.Tour;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class TourService {

    private final TourRepository tourRepository;
    private final ImageRepository imageRepository;
    private final LogService logService;

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

    public List<DetailedTour> getAllDetailedTours() {
        List<DetailedTour> ret = new ArrayList<>();
        try {
            for (var tourEntity : tourRepository.getAllTours()) {
                var tour = TourMapper.INSTANCE.tourEntityToDetailedTour(tourEntity);
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
                tour.setDistance(logService.getDistanceOfTour(id));
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
                existingTour = TourMapper.INSTANCE.combineDetailedTourWithTourEntity(existingTour, detailedTour);
                tourRepository.updateTour(existingTour);
            } else {
                // Insert
                var tourEntity = TourMapper.INSTANCE.detailedTourToTourEntity(detailedTour);

                if (tourEntity.getId() != 0) {
                    tourRepository.insertTourWithId(tourEntity);
                } else {
                    tourRepository.insertTour(tourEntity);
                }
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

    private String getImageOfTour(DetailedTour detailedTour) {
        var key = detailedTour.getId() + ".jpeg";
        if (imageRepository.hasImage(key)) {
            return Base64.getEncoder().encodeToString(imageRepository.getImage(key));
        }
        return "";
    }
}
