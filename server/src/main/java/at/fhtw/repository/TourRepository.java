package at.fhtw.repository;

import at.fhtw.repository.model.TourEntity;
import com.google.common.annotations.VisibleForTesting;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface TourRepository {
    List<TourEntity> getAllTours() throws SQLException;

    Optional<TourEntity> getTour(int id) throws SQLException;

    boolean tourExists(int id) throws SQLException;

    void deleteTour(int id) throws SQLException;

    void updateTour(TourEntity tourEntity) throws SQLException;

    void insertTour(TourEntity tourEntity) throws SQLException;

    void insertTourWithId(TourEntity tourEntity) throws SQLException;

    @VisibleForTesting
    void deleteAll() throws SQLException;
}
