package at.fhtw.repository;

import at.fhtw.repository.model.LogEntity;
import com.google.common.annotations.VisibleForTesting;
import lombok.SneakyThrows;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface LogRepository {
    @SneakyThrows
    List<LogEntity> getAllLogs();

    @SneakyThrows
    Optional<LogEntity> getLog(int id);

    boolean logExists(int id) throws SQLException;

    @SneakyThrows
    List<LogEntity> getLogsOfTour(int tourId);

    @SneakyThrows
    float getDistanceOfTour(int tourId);

    void updateLog(LogEntity logEntity) throws SQLException;

    void insertLog(LogEntity logEntity) throws SQLException;

    void insertLogWithId(LogEntity logEntity) throws SQLException;

    void deleteLog(int id) throws SQLException;

    @VisibleForTesting
    void deleteAll() throws SQLException;
}
