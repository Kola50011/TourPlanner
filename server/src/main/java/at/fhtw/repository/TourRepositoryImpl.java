package at.fhtw.repository;

import at.fhtw.repository.model.TourEntity;
import com.google.common.annotations.VisibleForTesting;
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
public class TourRepositoryImpl implements TourRepository {

    private final ConnectionFactory connectionFactory;

    @Override
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
                            .build());
                }
            }
        }
        return res;
    }

    @Override
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
                            .build());
                }
            }
        }
        return Optional.empty();
    }

    @Override
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

    @Override
    public void deleteTour(int id) throws SQLException {
        try (var connection = connectionFactory.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("delete from Tour where id = ?")) {
                statement.setInt(1, id);
                statement.execute();
            }
        }
    }

    @Override
    public void updateTour(TourEntity tourEntity) throws SQLException {
        try (var connection = connectionFactory.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("update Tour set " +
                    "name = ?, description = ? where id = ?")) {
                statement.setString(1, tourEntity.getName());
                statement.setString(2, tourEntity.getDescription());
                statement.setInt(3, tourEntity.getId());
                statement.executeUpdate();
            }
        }
    }

    @Override
    public void insertTour(TourEntity tourEntity) throws SQLException {
        try (var connection = connectionFactory.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("insert into Tour " +
                    "(name, description) " +
                    "values (?, ?)")) {
                statement.setString(1, tourEntity.getName());
                statement.setString(2, tourEntity.getDescription());

                statement.execute();
            }
        }
    }

    @Override
    public void insertTourWithId(TourEntity tourEntity) throws SQLException {
        try (var connection = connectionFactory.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("insert into Tour " +
                    "(name, description, id) " +
                    "values (?, ?, ?)")) {
                statement.setString(1, tourEntity.getName());
                statement.setString(2, tourEntity.getDescription());
                statement.setInt(3, tourEntity.getId());

                statement.execute();
            }
        }
    }

    @Override
    @VisibleForTesting
    public void deleteAll() throws SQLException {
        try (var connection = connectionFactory.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("delete from Tour")) {
                statement.execute();
            }
        }
    }
}
