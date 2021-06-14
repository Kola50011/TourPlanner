package at.fhtw.service;

import at.fhtw.client.MapQuestClient;
import at.fhtw.repository.ImageRepository;
import at.fhtw.repository.LogRepository;
import at.fhtw.service.mapper.LogMapper;
import at.fhtw.service.model.Log;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class LogService {

    private final LogRepository logRepository;
    private final MapQuestClient mapQuestClient;
    private final ImageRepository imageRepository;

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

    public float getDistanceOfTour(int tourId) {
        return logRepository.getDistanceOfTour(tourId);
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
            asyncUpdateRouteOfTour(newLog.getTourId());
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
                asyncUpdateRouteOfTour(log.get().getTourId());
            }
            return true;
        } catch (SQLException e) {
            log.error("Could not delete log!", e);
        }
        return false;
    }

    @SneakyThrows
    public void asyncUpdateRouteOfTour(int tourId) {
        new Thread(() -> updateRouteOfTour(tourId)).start();
    }

    private float calculateLogDistance(Log tourLog) throws IOException {
        var route = mapQuestClient.getRoute(List.of(tourLog.getStartLocation(), tourLog.getEndLocation()));
        if (route.getInfo().getStatuscode() == 500) {
            log.error("Error when getting log distance");
            return -1;
        }
        return route.getRoute().getDistance();
    }

    @SneakyThrows
    private void updateRouteOfTour(int tourId) {
        var key = tourId + ".jpeg";

        var logs = getLogsOfTour(tourId);
        if (logs.isEmpty()) {
            return;
        }

        var locations = logToLocations(logs);
        if (locations.size() < 2) {
            return;
        }
        var locationsString = String.join("||", locations);
        var route = mapQuestClient.getRoute(locations);
        var image = mapQuestClient.getMap(route.getRoute().getSessionId(), route.getRoute().getBoundingBox(), locationsString);

        imageRepository.putImage(key, image);
    }

    private List<String> logToLocations(List<Log> logs) {
        List<String> ret = new ArrayList<>();
        for (var log : logs) {
            var startLocation = log.getStartLocation();
            var endLocation = log.getEndLocation();

            if (ret.isEmpty()) {
                ret.add(startLocation);
                ret.add(endLocation);
            } else {
                if (!ret.get(ret.size() - 1).equalsIgnoreCase(startLocation)) {
                    ret.add(startLocation);
                }
                if (!ret.get(ret.size() - 1).equalsIgnoreCase(endLocation)) {
                    ret.add(endLocation);
                }
            }
        }
        ret = ret.stream().filter(location -> !location.isBlank()).collect(Collectors.toList());
        log.info(ret.toString());
        return ret;
    }
}
