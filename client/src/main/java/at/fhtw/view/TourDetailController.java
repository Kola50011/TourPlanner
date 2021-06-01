package at.fhtw.view;

import at.fhtw.client.TourPlannerClientFactory;
import at.fhtw.events.TourChangeEvent;
import at.fhtw.viewModel.TourDetailViewModel;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
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
    private Tab logsTab;

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

    @SneakyThrows
    public void reportClicked(ActionEvent actionEvent) {
        java.awt.Desktop.getDesktop().browse(new URI("http://localhost:8080/tours/" +
                tourDetailViewModel.getCurrentTour().getId() + "/report"));
    }

    @FXML
    private void initialize() {
        tourDetailViewModel = new TourDetailViewModel(TourPlannerClientFactory.getClient());

        nameField.textProperty().bindBidirectional(tourDetailViewModel.getNameField());
        totalDistanceLabel.textProperty().bindBidirectional(tourDetailViewModel.getTotalDistanceLabel());
        descriptionField.textProperty().bindBidirectional(tourDetailViewModel.getDescriptionField());

        routeImageView.imageProperty().bindBidirectional(tourDetailViewModel.getImageProperty());

        setupNameChangeListener();
        setupDescriptionChangeListener();
        setupLogTabsChangeListener();
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

    private void setupLogTabsChangeListener() {
        logsTab.selectedProperty().addListener(selected -> {
            if (!((ReadOnlyBooleanProperty) selected).getValue()) {
                tourDetailViewModel.updateCurrentTour();
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
