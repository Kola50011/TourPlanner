package at.fhtw.viewModel;

import at.fhtw.client.TourPlannerClient;
import at.fhtw.client.model.Log;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

public class LogListViewModel {
    private final TourPlannerClient tourPlannerClient;

    @Getter
    private final ListProperty<String> listProperty = new SimpleListProperty<>();
    private List<Log> logList = new ArrayList<>();
    private int tourId;

    public LogListViewModel(TourPlannerClient tourPlannerClient) {
        this.tourPlannerClient = tourPlannerClient;
    }

    public int logIndexToId(int idx) {
        return logList.get(idx).getId();
    }

    @SneakyThrows
    public void updateLogs() {
        logList = new ArrayList<>();
        var listKeys = new ArrayList<String>();

        var tours = tourPlannerClient.getLogsOfTour(tourId);
        for (var tour : tours) {
            listKeys.add(tour.getStartLocation() + " - " + tour.getEndLocation());
            logList.add(tour);
        }

        listProperty.set(FXCollections.observableArrayList(listKeys));
    }

    public void setTour(int tourId) {
        this.tourId = tourId;
        updateLogs();
    }

    public void addNewLog() {
        var log = Log.defaultLog();
        log.setTourId(tourId);
        tourPlannerClient.putLog(log);
        updateLogs();
    }

    public void deleteLog(int logId) {
        tourPlannerClient.deleteLog(logId);
        updateLogs();
    }
}
