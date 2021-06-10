package at.fhtw.view;

import javafx.fxml.FXML;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IndexController {

    @FXML
    private TourListController tourListController;

    @FXML
    private TourDetailController tourDetailController;

    @FXML
    private MenuController menuController;

    @FXML
    private SearchController searchController;

    @SneakyThrows
    @FXML
    public void initialize() {
        tourListController.addChangeEventListener(tourChangeEvent ->
                tourDetailController.setTour(tourChangeEvent.getTourId()));

        tourDetailController.addNameChangeEventListener(tourChangeEvent ->
                tourListController.updateTourList());

        menuController.addChangeEventListener(tourChangeEvent ->
                tourListController.updateTourList());

        searchController.addFilterEventHandler(filterEvent ->
                tourListController.setFilter(filterEvent.getFilter()));

        searchController.addFilterEventHandler(filterEvent ->
                tourDetailController.setFilter(filterEvent.getFilter()));
    }
}
