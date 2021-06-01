package at.fhtw.view;

import at.fhtw.client.TourPlannerClientFactory;
import at.fhtw.events.LogChangeEvent;
import at.fhtw.viewModel.LogListViewModel;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class LogListController {

    private final List<EventHandler<LogChangeEvent>> listChangedEventHandlers = new ArrayList<>();
    private final LogListViewModel logListViewModel = new LogListViewModel(TourPlannerClientFactory.getClient());

    @FXML
    private ListView<String> logListView;

    public void addChangeEventListener(EventHandler<LogChangeEvent> eventHandler) {
        listChangedEventHandlers.add(eventHandler);
//        selectedTourChanged();
    }

    public void logAddClicked(MouseEvent mouseEvent) {
        logListViewModel.addNewLog();
        var logIdx = logListView.getItems().size() - 1;
        logListView.getSelectionModel().select(logIdx);
        var logId = logListViewModel.logIndexToId(logIdx);
        fireChangeEvent(logId);
    }


    public void logDeleteClicked(MouseEvent mouseEvent) {
        var logIdx = logListView.getSelectionModel().getSelectedIndex();
        var LogId = logListViewModel.logIndexToId(logIdx);
        logListViewModel.deleteLog(LogId);

        logListView.getSelectionModel().select(0);
        LogId = logListViewModel.logIndexToId(0);
        fireChangeEvent(LogId);
    }

    public void setTour(int id) {
        logListViewModel.setTour(id);
        selectedLogChanged();
    }

    public void updateLogs() {
        logListViewModel.updateLogs();
    }

    private void fireChangeEvent(int logId) {
        var event = new LogChangeEvent(logId);
        for (EventHandler<LogChangeEvent> eventHandler : listChangedEventHandlers) {
            eventHandler.handle(event);
        }
    }

    private void selectedLogChanged() {
        var logIdx = logListView.getSelectionModel().getSelectedIndex();
        if (logIdx == -1) {
            logListView.getSelectionModel().select(0);
            logIdx = 0;
        }
        if (logListView.getItems().size() > 0) {
            var tourId = logListViewModel.logIndexToId(logIdx);
            fireChangeEvent(tourId);
        }
    }

    @FXML
    private void initialize() {
        logListView.itemsProperty().bindBidirectional(logListViewModel.getListProperty());
        setupListChangedListener();
    }

    private void setupListChangedListener() {
        logListView.getSelectionModel().selectedItemProperty().addListener((arg0, oldValue, newValue) -> {
            selectedLogChanged();
        });
    }
}
