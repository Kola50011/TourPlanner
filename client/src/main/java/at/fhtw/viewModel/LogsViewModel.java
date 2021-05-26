package at.fhtw.viewModel;

import at.fhtw.client.TourPlannerClient;
import at.fhtw.client.model.Log;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

public class LogsViewModel {
    private static LogsViewModel instance;

    @Getter
    private ObjectProperty<ObservableList<Log>> itemsProperty = new SimpleObjectProperty<>();

    @Getter
    private ObservableList<Log> items = FXCollections.observableArrayList();
    private TourPlannerClient tourPlannerClient;

    private int currentTour;

    public LogsViewModel(TourPlannerClient tourPlannerClient) {
        itemsProperty.setValue(items);
        this.tourPlannerClient = tourPlannerClient;
    }

    public void setCurrentTour(int id) {
        currentTour = id;
        updateItems();
    }

    private void updateItems() {
        var logs = tourPlannerClient.getLogsOfTour(currentTour);
        items.setAll(logs);
    }
}
