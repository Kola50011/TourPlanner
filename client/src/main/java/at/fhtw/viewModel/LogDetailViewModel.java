package at.fhtw.viewModel;

import at.fhtw.client.TourPlannerClient;
import at.fhtw.client.model.Log;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor
public class LogDetailViewModel {
    private final TourPlannerClient tourPlannerClient;

    private Log currentLog;

    @Getter
    private Property<LocalDate> startDate = new SimpleObjectProperty<>();

    public void setLog(int id) {
        currentLog = tourPlannerClient.get
    }
}
