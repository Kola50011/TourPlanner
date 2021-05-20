package at.fhtw.service.mapper;

import at.fhtw.repository.model.TourEntity;
import at.fhtw.service.model.DetailedTour;
import at.fhtw.service.model.Tour;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TourMapper {
    TourMapper INSTANCE = Mappers.getMapper(TourMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    TourEntity tourToTourEntity(Tour tour);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT)
    @Mapping(source = "name", target = "name", defaultValue = "")
    @Mapping(source = "description", target = "description", defaultValue = "")
    @Mapping(source = "distance", target = "distance", defaultValue = "-1.0")
    TourEntity detailedTourToTourEntity(DetailedTour tour);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(source = "detailedTour.id", target = "id")
    @Mapping(source = "detailedTour.name", target = "name")
    @Mapping(source = "detailedTour.description", target = "description")
    @Mapping(source = "detailedTour.distance", target = "distance")
    TourEntity combineDetailedTourWithTourEntity(@MappingTarget TourEntity tourEntity, DetailedTour detailedTour);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    Tour tourEntityToTour(TourEntity tourEntity);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "distance", target = "distance")
    DetailedTour tourEntityToDetailedTour(TourEntity tourEntity);
}
