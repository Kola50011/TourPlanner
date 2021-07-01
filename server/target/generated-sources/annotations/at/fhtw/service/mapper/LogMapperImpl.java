package at.fhtw.service.mapper;

import at.fhtw.repository.model.LogEntity;
import at.fhtw.service.model.Log;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-07-01T18:46:26+0200",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.7 (JetBrains s.r.o.)"
)
@Component
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
        logEntity.setDistance( log.getDistance() );
        logEntity.setNotes( log.getNotes() );
        logEntity.setMoneySpent( log.getMoneySpent() );

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
        log.setDistance( logEntity.getDistance() );
        log.setNotes( logEntity.getNotes() );
        log.setMoneySpent( logEntity.getMoneySpent() );

        return log;
    }

    @Override
    public LogEntity combineLogEntityWithLogEntity(LogEntity oldLog, Log newLog) {
        if ( newLog == null ) {
            return null;
        }

        oldLog.setId( newLog.getId() );
        oldLog.setTourId( newLog.getTourId() );
        if ( newLog.getStartTime() != null ) {
            oldLog.setStartTime( newLog.getStartTime() );
        }
        if ( newLog.getEndTime() != null ) {
            oldLog.setEndTime( newLog.getEndTime() );
        }
        if ( newLog.getStartLocation() != null ) {
            oldLog.setStartLocation( newLog.getStartLocation() );
        }
        if ( newLog.getEndLocation() != null ) {
            oldLog.setEndLocation( newLog.getEndLocation() );
        }
        oldLog.setRating( newLog.getRating() );
        if ( newLog.getMeansOfTransport() != null ) {
            oldLog.setMeansOfTransport( newLog.getMeansOfTransport() );
        }
        oldLog.setDistance( newLog.getDistance() );
        if ( newLog.getNotes() != null ) {
            oldLog.setNotes( newLog.getNotes() );
        }
        if ( newLog.getMoneySpent() != null ) {
            oldLog.setMoneySpent( newLog.getMoneySpent() );
        }

        return oldLog;
    }
}
