package at.fhtw.repository;

import at.fhtw.repository.model.TourEntity;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class TourRepository {

    private final ConnectionFactory connectionFactory;

    @SneakyThrows
    public List<TourEntity> getAllTours() {
        List<TourEntity> res = new ArrayList<>();
        try (var connection = connectionFactory.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from Tour")) {
                var resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    res.add(TourEntity.builder()
                            .id(resultSet.getInt("id"))
                            .name(resultSet.getString("name"))
                            .build());
                }
            }
        }
        return res;
    }

    @SneakyThrows
    public Optional<TourEntity> getTour(int id) {
        try (var connection = connectionFactory.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from Tour where id = ?")) {
                statement.setInt(1, id);
                var resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return Optional.of(TourEntity.builder()
                            .id(resultSet.getInt("id"))
                            .name(resultSet.getString("name"))
                            .build());
                }
            }
        }
        return Optional.empty();
    }
}
