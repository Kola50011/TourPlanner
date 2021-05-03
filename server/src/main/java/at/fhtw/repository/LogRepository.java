package at.fhtw.repository;

import at.fhtw.repository.model.LogEntity;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class LogRepository {

    private final ConnectionFactory connectionFactory;

    @SneakyThrows
    public List<LogEntity> getAllLogs() {
        List<LogEntity> res = new ArrayList<>();
        try (var connection = connectionFactory.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from Log")) {
                var resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    res.add(LogEntity.builder()
                            .id(resultSet.getInt("id"))
                            .tourId(resultSet.getInt("tourId"))
                            .startTime(resultSet.getTimestamp("startTime"))
                            .endTime(resultSet.getTimestamp("endTime"))
                            .startLocation(resultSet.getString("startLocation"))
                            .endLocation(resultSet.getString("endLocation"))
                            .endTime(resultSet.getTimestamp("endTime"))
                            .rating(resultSet.getInt("rating"))
                            .meansOfTransport(resultSet.getString("meansOfTransport"))
                            .build());
                }
            }
        }
        return res;
    }

    @SneakyThrows
    public List<LogEntity> getLogsOfTour(int tourId) {
        List<LogEntity> res = new ArrayList<>();
        try (var connection = connectionFactory.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from Log where tourid = ?")) {
                statement.setInt(1, tourId);
                var resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    res.add(LogEntity.builder()
                            .id(resultSet.getInt("id"))
                            .tourId(resultSet.getInt("tourId"))
                            .startTime(resultSet.getTimestamp("startTime"))
                            .endTime(resultSet.getTimestamp("endTime"))
                            .startLocation(resultSet.getString("startLocation"))
                            .endLocation(resultSet.getString("endLocation"))
                            .rating(resultSet.getInt("rating"))
                            .meansOfTransport(resultSet.getString("meansOfTransport"))
                            .build());
                }
            }
        }
        return res;
    }
}
