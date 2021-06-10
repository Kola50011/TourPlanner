package at.fhtw.view;

import at.fhtw.client.model.Filter;
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

    public void setFilter(Filter filter) {
        logListController.setFilter(filter);
    }

    @FXML
    private void initialize() {
        logListController.addChangeEventListener(logChangeEvent -> {
                    logDetailController.setLog(logChangeEvent.getLogId());
                }
        );

        logDetailController.addLogChangeEventHandler(logChangeEvent -> {
                    logListController.updateLogs();
                }
        );
    }
}
