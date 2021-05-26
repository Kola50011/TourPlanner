package at.fhtw.view;

import at.fhtw.client.TourPlannerClientFactory;
import at.fhtw.viewModel.LogDetailViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import tornadofx.control.DateTimePicker;

public class LogDetailController {
    private final LogDetailViewModel logDetailViewModel = new LogDetailViewModel(TourPlannerClientFactory.getClient());

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

    public void setLog(int id) {
        logDetailViewModel.setLog(id);
    }

    @FXML
    private void initialize() {
        startLocationField.textProperty().bindBidirectional(logDetailViewModel.getSimpleStringProperty());
        startDateField.dateTimeValueProperty().bindBidirectional(logDetailViewModel.getStartDateProperty());
        endLocationField.textProperty().bindBidirectional(logDetailViewModel.getSimpleStringProperty());
        endDateField.dateTimeValueProperty().bindBidirectional(logDetailViewModel.getStartDateProperty());
        meansOfTransportationComboBox.itemsProperty().bind(logDetailViewModel.getTransportationOptions());
        meansOfTransportationComboBox.valueProperty().bindBidirectional(logDetailViewModel.getMeansOfTransportationProperty());
        ratingSlider.valueProperty().bindBidirectional(logDetailViewModel.getRatingProperty());
        distanceLabel.textProperty().bindBidirectional(logDetailViewModel.getDistanceProperty());
    }
}
