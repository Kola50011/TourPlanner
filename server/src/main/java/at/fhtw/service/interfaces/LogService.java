package at.fhtw.service.interfaces;

import at.fhtw.service.model.Log;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Optional;

public interface LogService {
    List<Log> getLogs();

    List<Log> getLogsOfTour(int tourId);

    float getDistanceOfTour(int tourId);

    Optional<Log> getLog(int id);

    boolean insertOrUpdateLog(Log newLog);

    boolean deleteLog(int id);

    @SneakyThrows
    void asyncUpdateRouteOfTour(int tourId);
}
