package at.fhtw.service;

import at.fhtw.repository.TourRepository;
import at.fhtw.service.mapper.TourMapper;
import at.fhtw.service.model.Tour;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class TourService {

    private final TourRepository tourRepository;

    public List<Tour> getAllTours() {
        List<Tour> ret = new ArrayList<>();
        for (var tourEntity : tourRepository.getAllTours()) {
            var tour = TourMapper.INSTANCE.tourEntityToTour(tourEntity);
            ret.add(tour);
        }
        return ret;
    }

    public Optional<Tour> getTour(int id) {
        var tourEntity = tourRepository.getTour(id);
        if (tourEntity.isPresent()) {
            var tour = TourMapper.INSTANCE.tourEntityToTour(tourEntity.get());
            return Optional.of(tour);
        }
        return Optional.empty();
    }
}
