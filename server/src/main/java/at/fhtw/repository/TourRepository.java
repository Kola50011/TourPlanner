package at.fhtw.repository;

import at.fhtw.repository.model.TourEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class TourRepository {

    private final ConnectionFactory connectionFactory;

    public List<TourEntity> getAllTours() throws SQLException {
        List<TourEntity> res = new ArrayList<>();
        try (var connection = connectionFactory.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from Tour order by id")) {
                var resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    res.add(TourEntity.builder()
                            .id(resultSet.getInt("id"))
                            .name(resultSet.getString("name"))
                            .description(resultSet.getString("description"))
                            .distance(resultSet.getFloat("distance"))
                            .build());
                }
            }
        }
        return res;
    }

    public Optional<TourEntity> getTour(int id) throws SQLException {
        try (var connection = connectionFactory.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from Tour where id = ?")) {
                statement.setInt(1, id);
                var resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return Optional.of(TourEntity.builder()
                            .id(resultSet.getInt("id"))
                            .name(resultSet.getString("name"))
                            .description(resultSet.getString("description"))
                            .distance(resultSet.getFloat("distance"))
                            .build());
                }
            }
        }
        return Optional.empty();
    }

    public boolean tourExists(int id) throws SQLException {
        try (var connection = connectionFactory.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select count(*) as count from Tour where id = ?")) {
                statement.setInt(1, id);
                var resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getInt("count") == 1;
                }
            }
        }
        return false;
    }

    public void deleteTour(int id) throws SQLException {
        try (var connection = connectionFactory.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("delete from Tour where id = ?")) {
                statement.setInt(1, id);
                statement.execute();
            }
        }
    }

    public void updateTour(TourEntity tourEntity) throws SQLException {
        try (var connection = connectionFactory.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("update Tour set " +
                    "name = ?, description = ?, distance = ? where id = ?")) {
                statement.setString(1, tourEntity.getName());
                statement.setString(2, tourEntity.getDescription());
                statement.setFloat(3, tourEntity.getDistance());
                statement.setInt(4, tourEntity.getId());
                statement.executeUpdate();
            }
        }
    }

    public void insertTour(TourEntity tourEntity) throws SQLException {
        try (var connection = connectionFactory.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("insert into Tour " +
                    "(name, description, distance) " +
                    "values (?, ?, ?)")) {
                statement.setString(1, tourEntity.getName());
                statement.setString(2, tourEntity.getDescription());
                statement.setFloat(3, tourEntity.getDistance());
                statement.execute();
            }
        }
    }
}
