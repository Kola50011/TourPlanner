package at.fhtw.service.mapper;

import at.fhtw.repository.model.LogEntity;
import at.fhtw.service.model.Log;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-05-03T18:22:26+0200",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.7 (JetBrains s.r.o.)"
)
public class LogMapperImpl implements LogMapper {

    @Override
    public LogEntity logToLogEntity(Log log) {
        if ( log == null ) {
            return null;
        }

        LogEntity logEntity = new LogEntity();

        logEntity.setId( log.getId() );
        logEntity.setTourId( log.getTourId() );
        logEntity.setStartTime( log.getStartTime() );
        logEntity.setEndTime( log.getEndTime() );
        logEntity.setStartLocation( log.getStartLocation() );
        logEntity.setEndLocation( log.getEndLocation() );
        logEntity.setRating( log.getRating() );
        logEntity.setMeansOfTransport( log.getMeansOfTransport() );

        return logEntity;
    }

    @Override
    public Log logEntityToLog(LogEntity logEntity) {
        if ( logEntity == null ) {
            return null;
        }

        Log log = new Log();

        log.setId( logEntity.getId() );
        log.setTourId( logEntity.getTourId() );
        log.setStartTime( logEntity.getStartTime() );
        log.setEndTime( logEntity.getEndTime() );
        log.setStartLocation( logEntity.getStartLocation() );
        log.setEndLocation( logEntity.getEndLocation() );
        log.setRating( logEntity.getRating() );
        log.setMeansOfTransport( logEntity.getMeansOfTransport() );

        return log;
    }
}
