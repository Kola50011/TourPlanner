package at.fhtw.service.mapper;

import at.fhtw.repository.model.TourEntity;
import at.fhtw.service.model.Tour;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-04-29T20:50:20+0200",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.7 (JetBrains s.r.o.)"
)
public class TourMapperImpl implements TourMapper {

    @Override
    public TourEntity tourToTourEntity(Tour tour) {
        if ( tour == null ) {
            return null;
        }

        TourEntity tourEntity = new TourEntity();

        tourEntity.setId( tour.getId() );
        tourEntity.setName( tour.getName() );

        return tourEntity;
    }

    @Override
    public Tour tourEntityToTour(TourEntity tourEntity) {
        if ( tourEntity == null ) {
            return null;
        }

        Tour tour = new Tour();

        tour.setId( tourEntity.getId() );
        tour.setName( tourEntity.getName() );

        return tour;
    }
}
