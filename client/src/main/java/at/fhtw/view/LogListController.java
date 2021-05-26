package at.fhtw.view;

import at.fhtw.client.TourPlannerClient;
import at.fhtw.viewModel.LogListViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

public class LogListController {

    private final LogListViewModel logListViewModel = new LogListViewModel(new TourPlannerClient());

    @FXML
    private ListView<String> logListView;

    public void logListClicked(MouseEvent mouseEvent) {
    }

    public void logAddClicked(MouseEvent mouseEvent) {
    }

    public void logDeleteClicked(MouseEvent mouseEvent) {
    }

    public void setTour(int id) {
        logListViewModel.setTour(id);
    }

    @FXML
    private void initialize() {
        logListView.itemsProperty().bindBidirectional(logListViewModel.getListProperty());

        logListView.getSelectionModel().select(0);
    }
}
