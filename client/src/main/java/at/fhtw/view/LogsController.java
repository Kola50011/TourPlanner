package at.fhtw.view;

import at.fhtw.client.TourPlannerClient;
import at.fhtw.client.model.Log;
import at.fhtw.viewModel.LogsViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.DateCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class LogsController {
    @FXML
    private TableView<Log> tourTableView;
    @FXML
    private TableColumn<Log, DateCell> startTimeColumn;
    @FXML
    private TableColumn<Log, DateCell> endTimeColumn;
    @FXML
    private TableColumn<Log, String> startLocationColumn;
    @FXML
    private TableColumn<Log, String> endLocationColumn;
    @FXML
    private TableColumn<Log, Integer> ratingColumn;
    @FXML
    private TableColumn<Log, String> meansOfTransportColumn;
    @FXML
    private TableColumn<Log, Float> distanceColumn;
    private LogsViewModel logsViewModel;

    @FXML
    public void initialize() {
        logsViewModel = new LogsViewModel(new TourPlannerClient());
        tourTableView.setItems(logsViewModel.getItems());

        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        startLocationColumn.setCellValueFactory(new PropertyValueFactory<>("startLocation"));
        endLocationColumn.setCellValueFactory(new PropertyValueFactory<>("endLocation"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        meansOfTransportColumn.setCellValueFactory(new PropertyValueFactory<>("meansOfTransport"));
        distanceColumn.setCellValueFactory(new PropertyValueFactory<>("distance"));

//        startTimeColumn.setCellFactory(logDateCellTableColumn ->);
    }

    public void setTour(int id) {
        logsViewModel.setCurrentTour(id);
    }
}
