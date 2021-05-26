package at.fhtw.view;

import at.fhtw.client.TourPlannerClient;
import at.fhtw.viewModel.LogDetailViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class LogDetailController {
    private final LogDetailViewModel logDetailViewModel = new LogDetailViewModel(new TourPlannerClient());

    @FXML
    private DatePicker startDateField;
    @FXML
    private TextField startLocationField;

    public void setTour(int id) {
        logDetailViewModel.set
    }

    @FXML
    private void initialize() {
        startDateField.valueProperty().bindBidirectional(logDetailViewModel.getStartDate());
    }
}
