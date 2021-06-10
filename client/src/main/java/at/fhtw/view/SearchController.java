package at.fhtw.view;

import at.fhtw.client.TourPlannerClientFactory;
import at.fhtw.client.model.SearchResult;
import at.fhtw.events.FilterEvent;
import at.fhtw.viewModel.SearchViewModel;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class SearchController {
    private final List<EventHandler<FilterEvent>> filterEventHandlers = new ArrayList<>();

    @FXML
    private TextField searchField;
    private SearchViewModel searchViewModel;

    public void searchClicked() {
        fireFilterEvent(searchViewModel.search());
    }

    public void addFilterEventHandler(EventHandler<FilterEvent> eventHandler) {
        filterEventHandlers.add(eventHandler);
    }

    public void clearClicked() {
        searchField.setText("");
        fireFilterEvent(new SearchResult());
    }

    @FXML
    private void initialize() {
        searchViewModel = new SearchViewModel(TourPlannerClientFactory.getClient());

        searchField.textProperty().bindBidirectional(searchViewModel.getSearchField());
    }

    private void fireFilterEvent(SearchResult searchResult) {
        var event = new FilterEvent(searchResult);
        for (EventHandler<FilterEvent> eventHandler : filterEventHandlers) {
            eventHandler.handle(event);
        }
    }
}
