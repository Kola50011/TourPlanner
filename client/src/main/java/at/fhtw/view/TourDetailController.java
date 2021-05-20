package at.fhtw.view;

import at.fhtw.client.TourPlannerClient;
import at.fhtw.events.TourChangeEvent;
import at.fhtw.viewModel.TourDetailViewModel;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;


@Slf4j
public class TourDetailController {

    private final List<EventHandler<TourChangeEvent>> nameChangedEventHandlers = new ArrayList<>();
    @FXML
    public LogsController logsController;
    @FXML
    private TextField nameField;
    @FXML
    private Label totalDistanceLabel;
    @FXML
    private TextArea descriptionField;

    @FXML
    private ImageView routeImageView;

    private TourDetailViewModel tourDetailViewModel;

    public void setTour(int id) {
        tourDetailViewModel.setTour(id);
        logsController.setTour(id);
    }

    public void addNameChangeEventListener(EventHandler<TourChangeEvent> eventHandler) {
        nameChangedEventHandlers.add(eventHandler);
    }

    @FXML
    private void initialize() {
        tourDetailViewModel = new TourDetailViewModel(new TourPlannerClient());

        nameField.textProperty().bindBidirectional(tourDetailViewModel.getNameField());
        totalDistanceLabel.textProperty().bindBidirectional(tourDetailViewModel.getTotalDistanceLabel());
        descriptionField.textProperty().bindBidirectional(tourDetailViewModel.getDescriptionField());

        routeImageView.imageProperty().bindBidirectional(tourDetailViewModel.getImageProperty());

        setupNameChangeListener();
        setupDescriptionChangeListener();
    }

    private void setupNameChangeListener() {
        nameField.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (!newPropertyValue) {
                tourDetailViewModel.updateTourName();
                fireChangeEvent(tourDetailViewModel.getCurrentTour().getId());
            }
        });
    }

    private void setupDescriptionChangeListener() {
        descriptionField.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (!newPropertyValue) {
                tourDetailViewModel.updateTourDescription();
            }
        });
    }

    private void fireChangeEvent(int tourId) {
        var event = new TourChangeEvent(tourId);
        for (EventHandler<TourChangeEvent> eventHandler : nameChangedEventHandlers) {
            eventHandler.handle(event);
        }
    }
}
