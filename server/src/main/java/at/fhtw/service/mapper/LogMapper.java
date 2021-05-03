package at.fhtw.service.mapper;

import at.fhtw.repository.model.LogEntity;
import at.fhtw.service.model.Log;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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
    LogEntity logToLogEntity(Log log);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "tourId", target = "tourId")
    @Mapping(source = "startTime", target = "startTime")
    @Mapping(source = "endTime", target = "endTime")
    @Mapping(source = "startLocation", target = "startLocation")
    @Mapping(source = "endLocation", target = "endLocation")
    @Mapping(source = "rating", target = "rating")
    @Mapping(source = "meansOfTransport", target = "meansOfTransport")
    Log logEntityToLog(LogEntity logEntity);

}
