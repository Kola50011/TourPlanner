package at.fhtw.service.interfaces;

import at.fhtw.service.model.DetailedTour;
import at.fhtw.service.model.Tour;

import java.util.List;
import java.util.Optional;

public interface TourService {
    List<Tour> getAllTours();

    List<DetailedTour> getAllDetailedTours();

    Optional<DetailedTour> getTour(int id);

    boolean insertOrUpdateTour(DetailedTour detailedTour);

    boolean deleteTour(int id);
}
