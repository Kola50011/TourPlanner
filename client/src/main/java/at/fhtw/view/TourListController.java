package at.fhtw.view;

import at.fhtw.client.TourPlannerClientFactory;
import at.fhtw.client.model.Filter;
import at.fhtw.events.TourChangeEvent;
import at.fhtw.viewModel.TourListViewModel;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TourListController {
    private final List<EventHandler<TourChangeEvent>> listChangedEventHandlers = new ArrayList<>();
    private final TourListViewModel tourListViewModel = new TourListViewModel(TourPlannerClientFactory.getClient());

    @FXML
    private ListView<String> tourListView;

    public void addChangeEventListener(EventHandler<TourChangeEvent> eventHandler) {
        listChangedEventHandlers.add(eventHandler);
    }

    public void updateTourList() {
        tourListViewModel.updateTours();
    }

    public void setFilter(Filter filter) {
        tourListViewModel.setToursFilter(filter.getTourIDs());
        tourListViewModel.updateTours();
    }

    private void selectedTourChanged() {
        if (tourListView.getItems().size() != 0) {
            var tourIdx = tourListView.getSelectionModel().getSelectedIndex();
            if (tourIdx == -1) {
                tourListView.getSelectionModel().select(0);
                tourIdx = 0;
            }
            var tourId = tourListViewModel.tourIndexToId(tourIdx);
            fireChangeEvent(tourId);
        } else {
            fireChangeEvent(-1);
        }
    }

    @FXML
    private void initialize() {
        tourListView.itemsProperty().bindBidirectional(tourListViewModel.getTourNamesProperty());

        setupListChangedListener();
    }

    private void selectFirstTour() {
        if (tourListView.getItems().size() != 0) {
            tourListView.getSelectionModel().select(0);
            selectedTourChanged();
        }
    }

    private void setupListChangedListener() {
        tourListView.getSelectionModel().selectedItemProperty().addListener((arg0, oldValue, newValue) -> {
            selectedTourChanged();
        });
    }

    @FXML
    private void tourAddClicked() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Tour");
        dialog.setHeaderText("Create new Tour");
        dialog.setContentText("Name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            tourListViewModel.addTour(name);

            var tourIdx = tourListView.getItems().size() - 1;
            tourListView.getSelectionModel().select(tourIdx);
            var tourId = tourListViewModel.tourIndexToId(tourIdx);
            fireChangeEvent(tourId);
        });
    }

    @FXML
    private void tourDeleteClicked() {
        var tourIdx = tourListView.getSelectionModel().getSelectedIndex();
        var tourId = tourListViewModel.tourIndexToId(tourIdx);
        tourListViewModel.deleteTour(tourId);

        selectFirstTour();
    }

    private void fireChangeEvent(int tourId) {
        var event = new TourChangeEvent(tourId);
        for (EventHandler<TourChangeEvent> eventHandler : listChangedEventHandlers) {
            eventHandler.handle(event);
        }
    }
}
