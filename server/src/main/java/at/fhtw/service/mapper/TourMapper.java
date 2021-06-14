package at.fhtw.service.mapper;

import at.fhtw.repository.model.TourEntity;
import at.fhtw.service.model.DetailedTour;
import at.fhtw.service.model.Tour;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TourMapper {
    TourMapper INSTANCE = Mappers.getMapper(TourMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT)
    @Mapping(source = "name", target = "name", defaultValue = "")
    @Mapping(source = "description", target = "description", defaultValue = "")
    TourEntity detailedTourToTourEntity(DetailedTour tour);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(source = "detailedTour.id", target = "id")
    @Mapping(source = "detailedTour.name", target = "name")
    @Mapping(source = "detailedTour.description", target = "description")
    TourEntity combineDetailedTourWithTourEntity(@MappingTarget TourEntity tourEntity, DetailedTour detailedTour);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    Tour tourEntityToTour(TourEntity tourEntity);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    DetailedTour tourEntityToDetailedTour(TourEntity tourEntity);
}
