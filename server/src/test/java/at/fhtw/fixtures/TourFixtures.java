package at.fhtw.fixtures;

import at.fhtw.repository.model.TourEntity;
import at.fhtw.service.model.DetailedTour;
import at.fhtw.service.model.Tour;

public class TourFixtures {

    public static Tour carTour() {
        return Tour.builder()
                .id(1)
                .name("Car Tour")
                .build();
    }

    public static Tour bikeTour() {
        return Tour.builder()
                .id(2)
                .name("Bike Tour")
                .build();
    }

    public static TourEntity carTourEntity() {
        return TourEntity.builder()
                .id(1)
                .name("Car Tour")
                .description("")
                .build();
    }

    public static TourEntity bikeTourEntity() {
        return TourEntity.builder()
                .id(2)
                .name("Bike Tour")
                .description("")
                .build();
    }

    public static DetailedTour detailedCarTour() {
        return DetailedTour.builder()
                .id(1)
                .name("Car Tour")
                .description("")
                .build();
    }

    public static DetailedTour detailedBikeTour() {
        return DetailedTour.builder()
                .id(2)
                .name("Bike Tour")
                .description("")
                .build();
    }
}
