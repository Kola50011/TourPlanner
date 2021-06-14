package at.fhtw.service.mapper;

import at.fhtw.repository.model.LogEntity;
import at.fhtw.service.model.Log;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LogMapper {
    LogMapper INSTANCE = Mappers.getMapper(LogMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "tourId", target = "tourId")
    @Mapping(source = "startTime", target = "startTime")
    @Mapping(source = "endTime", target = "endTime")
    @Mapping(source = "startLocation", target = "startLocation")
    @Mapping(source = "endLocation", target = "endLocation")
    @Mapping(source = "rating", target = "rating")
    @Mapping(source = "meansOfTransport", target = "meansOfTransport")
    @Mapping(source = "distance", target = "distance")
    LogEntity logToLogEntity(Log log);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "tourId", target = "tourId")
    @Mapping(source = "startTime", target = "startTime")
    @Mapping(source = "endTime", target = "endTime")
    @Mapping(source = "startLocation", target = "startLocation")
    @Mapping(source = "endLocation", target = "endLocation")
    @Mapping(source = "rating", target = "rating")
    @Mapping(source = "meansOfTransport", target = "meansOfTransport")
    @Mapping(source = "distance", target = "distance")
    @Mapping(source = "notes", target = "notes")
    @Mapping(source = "moneySpent", target = "moneySpent")
    Log logEntityToLog(LogEntity logEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(source = "newLog.id", target = "id")
    @Mapping(source = "newLog.tourId", target = "tourId")
    @Mapping(source = "newLog.startTime", target = "startTime")
    @Mapping(source = "newLog.endTime", target = "endTime")
    @Mapping(source = "newLog.startLocation", target = "startLocation")
    @Mapping(source = "newLog.endLocation", target = "endLocation")
    @Mapping(source = "newLog.rating", target = "rating")
    @Mapping(source = "newLog.meansOfTransport", target = "meansOfTransport")
    @Mapping(source = "newLog.distance", target = "distance")
    @Mapping(source = "newLog.notes", target = "notes")
    @Mapping(source = "newLog.moneySpent", target = "moneySpent")
    LogEntity combineLogEntityWithLogEntity(@MappingTarget LogEntity oldLog, Log newLog);
}
