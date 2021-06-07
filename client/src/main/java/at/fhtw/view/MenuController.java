package at.fhtw.view;

import at.fhtw.client.TourPlannerClientFactory;
import at.fhtw.events.TourChangeEvent;
import at.fhtw.viewModel.MenuViewModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import lombok.SneakyThrows;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class MenuController {

    private final List<EventHandler<TourChangeEvent>> listChangedEventHandlers = new ArrayList<>();

    @FXML
    private AnchorPane menuAnchorPane;
    private MenuViewModel menuViewModel;

    public void addChangeEventListener(EventHandler<TourChangeEvent> eventHandler) {
        listChangedEventHandlers.add(eventHandler);
    }

    public void onExportClicked(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose save location");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON", "*.json")
        );

        var outputFile = fileChooser.showSaveDialog(menuAnchorPane.getScene().getWindow());
        menuViewModel.exportData(outputFile);
    }

    public void onImportClicked(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose import location");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON", "*.json")
        );

        var outputFile = fileChooser.showOpenDialog(menuAnchorPane.getScene().getWindow());
        menuViewModel.importData(outputFile);
        fireChangeEvent();
    }

    @SneakyThrows
    public void onReportClicked(ActionEvent actionEvent) {
        java.awt.Desktop.getDesktop().browse(new URI("http://localhost:8080/logs/report"));
    }

    private void fireChangeEvent() {
        var event = new TourChangeEvent(0);
        for (EventHandler<TourChangeEvent> eventHandler : listChangedEventHandlers) {
            eventHandler.handle(event);
        }
    }

    @FXML
    private void initialize() {
        menuViewModel = new MenuViewModel(TourPlannerClientFactory.getClient());
    }
}
