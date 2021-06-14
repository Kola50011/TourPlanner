package at.fhtw.viewModel;

import at.fhtw.client.TourPlannerClient;
import at.fhtw.client.model.Log;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class LogDetailViewModel {
    @Getter
    private final ListProperty<String> transportationOptions =
            new SimpleListProperty<>(FXCollections.observableArrayList(
                    "Car",
                    "Bicycle",
                    "Plane",
                    "Train",
                    "Foot")
            );

    private final TourPlannerClient tourPlannerClient;
    @Getter
    private final Property<LocalDateTime> startDateProperty = new SimpleObjectProperty<>();
    @Getter
    private final StringProperty startLocationProperty = new SimpleStringProperty();
    @Getter
    private final Property<LocalDateTime> endDateProperty = new SimpleObjectProperty<>();
    @Getter
    private final StringProperty endLocationProperty = new SimpleStringProperty();
    @Getter
    private final DoubleProperty ratingProperty = new SimpleDoubleProperty();
    @Getter
    private final StringProperty meansOfTransportationProperty = new SimpleStringProperty();
    @Getter
    private final StringProperty distanceProperty = new SimpleStringProperty();
    @Getter
    private final StringProperty notesProperty = new SimpleStringProperty();
    @Getter
    private final StringProperty moneySpentProperty = new SimpleStringProperty();

    @Getter
    private Log currentLog;

    public void setLog(int id) {
        var optionalLog = tourPlannerClient.getLog(id);
        optionalLog.ifPresentOrElse(log -> {
            currentLog = log;
            startDateProperty.setValue(currentLog.getStartTime().toLocalDateTime());
            startLocationProperty.setValue(currentLog.getStartLocation());
            endDateProperty.setValue(currentLog.getEndTime().toLocalDateTime());
            endLocationProperty.setValue(currentLog.getEndLocation());
            ratingProperty.setValue(currentLog.getRating());
            meansOfTransportationProperty.setValue(currentLog.getMeansOfTransport());
            distanceProperty.setValue(String.valueOf(currentLog.getDistance()));
            moneySpentProperty.setValue(String.valueOf(currentLog.getMoneySpent()));
            notesProperty.setValue(String.valueOf(currentLog.getNotes()));
        }, this::clearFields);
    }

    public void saveLog() {
        currentLog.setStartTime(Timestamp.valueOf(startDateProperty.getValue()));
        currentLog.setStartLocation(startLocationProperty.getValue());
        currentLog.setEndTime(Timestamp.valueOf(endDateProperty.getValue()));
        currentLog.setEndLocation(endLocationProperty.getValue());
        currentLog.setRating(ratingProperty.getValue().intValue());
        currentLog.setMeansOfTransport(meansOfTransportationProperty.getValue());
        currentLog.setMoneySpent(moneySpentProperty.getValue());
        currentLog.setNotes(notesProperty.getValue());

        tourPlannerClient.putLog(currentLog);

        setLog(currentLog.getId());
    }

    private void clearFields() {
        startDateProperty.setValue(LocalDateTime.now());
        startLocationProperty.setValue("");
        endDateProperty.setValue(LocalDateTime.now());
        endLocationProperty.setValue("");
        ratingProperty.setValue(0);
        meansOfTransportationProperty.setValue("");
        distanceProperty.setValue("");
        moneySpentProperty.setValue("");
        notesProperty.setValue("");
    }
}
