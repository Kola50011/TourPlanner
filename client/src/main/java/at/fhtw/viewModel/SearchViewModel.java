package at.fhtw.viewModel;

import at.fhtw.client.TourPlannerClient;
import at.fhtw.client.model.SearchRequest;
import at.fhtw.client.model.SearchResult;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class SearchViewModel {
    private final TourPlannerClient tourPlannerClient;

    @Getter
    private final StringProperty searchField = new SimpleStringProperty();

    public SearchResult search() {
        return tourPlannerClient.search(SearchRequest.builder()
                .searchString(searchField.get()).build());
    }
}
