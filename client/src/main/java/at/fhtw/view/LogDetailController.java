package at.fhtw.view;

import at.fhtw.client.TourPlannerClientFactory;
import at.fhtw.events.LogChangeEvent;
import at.fhtw.viewModel.LogDetailViewModel;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import lombok.extern.slf4j.Slf4j;
import tornadofx.control.DateTimePicker;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class LogDetailController {
    private final LogDetailViewModel logDetailViewModel = new LogDetailViewModel(TourPlannerClientFactory.getClient());

    private final List<EventHandler<LogChangeEvent>> logChangeEventHandlers = new ArrayList<>();

    @FXML
    private DateTimePicker startDateField;
    @FXML
    private TextField startLocationField;
    @FXML
    private DateTimePicker endDateField;
    @FXML
    private TextField endLocationField;
    @FXML
    private Slider ratingSlider;
    @FXML
    private ComboBox<String> meansOfTransportationComboBox;
    @FXML
    private Label distanceLabel;
    @FXML
    private GridPane logDetailPane;

    public void setLog(int id) {
        if (id == -1) {
            logDetailPane.setVisible(false);
        } else {
            logDetailPane.setVisible(true);
            logDetailViewModel.setLog(id);
        }
    }

    public void addLogChangeEventHandler(EventHandler<LogChangeEvent> eventHandler) {
        logChangeEventHandlers.add(eventHandler);
    }

    @FXML
    private void initialize() {
        startLocationField.textProperty().bindBidirectional(logDetailViewModel.getStartLocationProperty());
        startDateField.dateTimeValueProperty().bindBidirectional(logDetailViewModel.getStartDateProperty());
        endLocationField.textProperty().bindBidirectional(logDetailViewModel.getEndLocationProperty());
        endDateField.dateTimeValueProperty().bindBidirectional(logDetailViewModel.getEndDateProperty());
        meansOfTransportationComboBox.itemsProperty().bind(logDetailViewModel.getTransportationOptions());
        meansOfTransportationComboBox.valueProperty().bindBidirectional(logDetailViewModel.getMeansOfTransportationProperty());
        ratingSlider.valueProperty().bindBidirectional(logDetailViewModel.getRatingProperty());
        distanceLabel.textProperty().bindBidirectional(logDetailViewModel.getDistanceProperty());
    }

    @FXML
    private void saveButtonClicked() {
        logDetailViewModel.saveLog();
        fireChangeEvent(logDetailViewModel.getCurrentLog().getId());
    }

    private void fireChangeEvent(int logId) {
        var event = new LogChangeEvent(logId);
        for (EventHandler<LogChangeEvent> eventHandler : logChangeEventHandlers) {
            eventHandler.handle(event);
        }
    }

}
