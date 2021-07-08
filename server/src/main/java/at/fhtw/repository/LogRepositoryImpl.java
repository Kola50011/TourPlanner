package at.fhtw.repository;

import at.fhtw.repository.model.LogEntity;
import com.google.common.annotations.VisibleForTesting;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class LogRepositoryImpl implements LogRepository {

    private final ConnectionFactory connectionFactory;

    @Override
    @SneakyThrows
    public List<LogEntity> getAllLogs() {
        List<LogEntity> res = new ArrayList<>();
        try (var connection = connectionFactory.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from Log ")) {
                var resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    res.add(resultSetToLogEntity(resultSet));
                }
            }
        }
        return res;
    }

    @Override
    @SneakyThrows
    public Optional<LogEntity> getLog(int id) {
        try (var connection = connectionFactory.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from Log where id = ?")) {
                statement.setInt(1, id);
                var resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return Optional.of(resultSetToLogEntity(resultSet));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean logExists(int id) throws SQLException {
        try (var connection = connectionFactory.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select count(*) as count from Log where id = ?")) {
                statement.setInt(1, id);
                var resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getInt("count") == 1;
                }
            }
        }
        return false;
    }

    @Override
    @SneakyThrows
    public List<LogEntity> getLogsOfTour(int tourId) {
        List<LogEntity> res = new ArrayList<>();
        try (var connection = connectionFactory.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from Log where tourid = ? order by startTime")) {
                statement.setInt(1, tourId);
                var resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    res.add(resultSetToLogEntity(resultSet));
                }
            }
        }
        return res;
    }

    @Override
    @SneakyThrows
    public float getDistanceOfTour(int tourId) {
        float distance = 0;
        try (var connection = connectionFactory.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select sum(distance) as distance " +
                    "from Log where tourid = ?")) {
                statement.setInt(1, tourId);
                var resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getFloat("distance");
                }
            }
        }
        return distance;
    }

    @Override
    public void updateLog(LogEntity logEntity) throws SQLException {
        try (var connection = connectionFactory.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("update Log set " +
                    "tourId = ?, startTime = ?, startLocation = ?, endTime = ?, endLocation = ?," +
                    "rating = ?, meansOfTransport = ?, distance = ?, notes = ?, moneySpent = ? " +
                    "where id = ?")) {
                fillPreparedStatementWithLogEntity(statement, logEntity);
                statement.setInt(11, logEntity.getId());
                statement.executeUpdate();
            }
        }
    }

    @Override
    public void insertLog(LogEntity logEntity) throws SQLException {
        try (var connection = connectionFactory.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("insert into Log " +
                    "(tourid, startTime, startLocation, endTime, endLocation, " +
                    "rating, meansoftransport, distance, notes, moneySpent) " +
                    "values (?, ?, ?, ?, ?, ?, ? ,?, ?, ?)")) {
                fillPreparedStatementWithLogEntity(statement, logEntity);
                statement.execute();
            }
        }
    }

    @Override
    public void insertLogWithId(LogEntity logEntity) throws SQLException {
        try (var connection = connectionFactory.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("insert into Log " +
                    "(tourid, startTime, startLocation, endTime, endLocation, " +
                    "rating, meansoftransport, distance, notes, moneySpent, id) " +
                    "values (?, ?, ?, ?, ?, ?, ? ,?, ?, ?, ?)")) {
                fillPreparedStatementWithLogEntity(statement, logEntity);
                statement.setInt(11, logEntity.getId());
                statement.execute();
            }
        }
    }

    @Override
    public void deleteLog(int id) throws SQLException {
        try (var connection = connectionFactory.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("delete from Log where id = ?")) {
                statement.setInt(1, id);
                statement.execute();
            }
        }
    }

    @Override
    @VisibleForTesting
    public void deleteAll() throws SQLException {
        try (var connection = connectionFactory.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("delete from Log")) {
                statement.execute();
            }
        }
    }

    private void fillPreparedStatementWithLogEntity(PreparedStatement statement, LogEntity logEntity) throws SQLException {
        statement.setInt(1, logEntity.getTourId());
        statement.setTimestamp(2, logEntity.getStartTime());
        statement.setString(3, logEntity.getStartLocation());
        statement.setTimestamp(4, logEntity.getEndTime());
        statement.setString(5, logEntity.getEndLocation());
        statement.setInt(6, logEntity.getRating());
        statement.setString(7, logEntity.getMeansOfTransport());
        statement.setFloat(8, logEntity.getDistance());
        statement.setString(9, logEntity.getNotes());
        statement.setString(10, logEntity.getMoneySpent());

    }

    private LogEntity resultSetToLogEntity(ResultSet resultSet) throws SQLException {
        return LogEntity.builder()
                .id(resultSet.getInt("id"))
                .tourId(resultSet.getInt("tourId"))
                .startTime(resultSet.getTimestamp("startTime"))
                .endTime(resultSet.getTimestamp("endTime"))
                .startLocation(resultSet.getString("startLocation"))
                .endLocation(resultSet.getString("endLocation"))
                .endTime(resultSet.getTimestamp("endTime"))
                .rating(resultSet.getInt("rating"))
                .meansOfTransport(resultSet.getString("meansOfTransport"))
                .distance(resultSet.getFloat("distance"))
                .notes(resultSet.getString("notes"))
                .moneySpent(resultSet.getString("moneySpent"))
                .build();
    }
}
