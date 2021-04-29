package at.fhtw.service.mapper;

import at.fhtw.repository.model.TourEntity;
import at.fhtw.service.model.Tour;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TourMapper {
    TourMapper INSTANCE = Mappers.getMapper(TourMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    TourEntity tourToTourEntity(Tour tour);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    Tour tourEntityToTour(TourEntity tourEntity);
}
