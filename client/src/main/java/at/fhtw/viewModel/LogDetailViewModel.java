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
    private final StringProperty simpleStringProperty = new SimpleStringProperty();
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
    private Log currentLog;

    public void setLog(int id) {
        var optinoalLog = tourPlannerClient.getLog(id);
        optinoalLog.ifPresent(log -> {
            currentLog = log;
            startDateProperty.setValue(currentLog.getStartTime().toLocalDateTime());
            simpleStringProperty.setValue(currentLog.getStartLocation());
            endDateProperty.setValue(currentLog.getEndTime().toLocalDateTime());
            endLocationProperty.setValue(currentLog.getEndLocation());
            ratingProperty.setValue(currentLog.getRating());
            meansOfTransportationProperty.setValue(currentLog.getMeansOfTransport());
            distanceProperty.setValue(String.valueOf(currentLog.getDistance()));
        });
    }
}
