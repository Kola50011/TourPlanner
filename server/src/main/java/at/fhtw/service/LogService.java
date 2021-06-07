package at.fhtw.service;

import at.fhtw.client.MapQuestClient;
import at.fhtw.repository.LogRepository;
import at.fhtw.service.mapper.LogMapper;
import at.fhtw.service.model.Log;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class LogService {

    private final LogRepository logRepository;
    private final MapQuestClient mapQuestClient;

    @Autowired
    private TourService tourService;

    public List<Log> getLogs() {
        List<Log> ret = new ArrayList<>();
        for (var logEntity : (logRepository.getAllLogs())) {
            var log = LogMapper.INSTANCE.logEntityToLog(logEntity);
            ret.add(log);
        }
        return ret;
    }

    public List<Log> getLogsOfTour(int tourId) {
        List<Log> ret = new ArrayList<>();
        for (var logEntity : (logRepository.getLogsOfTour(tourId))) {
            var log = LogMapper.INSTANCE.logEntityToLog(logEntity);
            ret.add(log);
        }
        return ret;
    }

    public Optional<Log> getLog(int id) {
        var logEntity = logRepository.getLog(id);
        return logEntity.map(LogMapper.INSTANCE::logEntityToLog);
    }

    public boolean insertOrUpdateLog(Log newLog) {
        try {
            if (logRepository.logExists(newLog.getId())) {
                // Update
                var optionalLogEntity = logRepository.getLog(newLog.getId());
                if (optionalLogEntity.isEmpty()) {
                    return false;
                }
                log.debug("Updating log");

                var existingLog = optionalLogEntity.get();

                if (!newLog.getStartLocation().equalsIgnoreCase(existingLog.getStartLocation()) ||
                        !newLog.getEndLocation().equalsIgnoreCase(existingLog.getEndLocation())) {
                    newLog.setDistance(calculateLogDistance(newLog));
                    log.debug("Updating log distance");
                }

                var newLogEntity = LogMapper.INSTANCE.combineLogEntityWithLogEntity(existingLog, newLog);


                logRepository.updateLog(newLogEntity);
            } else {
                // Insert
                var tourEntity = LogMapper.INSTANCE.logToLogEntity(newLog);
                tourEntity.setDistance(calculateLogDistance(newLog));

                logRepository.insertTour(tourEntity);
            }
            tourService.asyncUpdateRouteOfTour(newLog.getTourId());
        } catch (SQLException e) {
            log.error("Unable to insert or update log! ", e);
            return false;
        } catch (IOException e) {
            log.error("IOException when updating log!", e);
            return false;
        }
        return true;
    }

    public boolean deleteLog(int id) {
        try {
            var log = getLog(id);
            if (log.isPresent()) {
                logRepository.deleteLog(id);
                tourService.asyncUpdateRouteOfTour(log.get().getTourId());
            }
            return true;
        } catch (SQLException e) {
            log.error("Could not delete log!", e);
        }
        return false;
    }

    private float calculateLogDistance(Log tourLog) throws IOException {
        var route = mapQuestClient.getRoute(List.of(tourLog.getStartLocation(), tourLog.getEndLocation()));
        if (route.getInfo().getStatuscode() == 500) {
            log.error("Error when getting log distance");
            return -1;
        }
        return route.getRoute().getDistance();
    }
}
