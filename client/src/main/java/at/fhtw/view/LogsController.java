package at.fhtw.view;

import javafx.fxml.FXML;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogsController {

    @FXML
    private LogListController logListController;

    public void setTour(int id) {
        logListController.setTour(id);
    }
}
