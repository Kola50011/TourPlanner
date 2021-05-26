package at.fhtw.view;

import javafx.fxml.FXML;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogsController {

    @FXML
    private LogListController logListController;

    @FXML
    private LogDetailController logDetailController;

    public void setTour(int id) {
        logListController.setTour(id);
    }

    @FXML
    private void initialize() {
        logListController.addChangeEventListener(logChangeEvent -> {
                    logDetailController.setLog(logChangeEvent.getLogId());
                }
        );
    }
}
