package at.fhtw.viewModel;

import at.fhtw.client.TourPlannerClient;
import at.fhtw.client.model.DetailedTour;
import at.fhtw.client.model.Tour;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TourListViewModel {
    private final TourPlannerClient tourPlannerClient;
    @Getter
    private final ListProperty<String> tourNamesProperty = new SimpleListProperty<>();

    private List<Tour> toursList;

    @Setter
    private Set<String> toursFilter = new HashSet<>();


    public TourListViewModel(TourPlannerClient tourPlannerClient) {
        this.tourPlannerClient = tourPlannerClient;
        updateTours();
    }

    public int tourIndexToId(int idx) {
        return toursList.get(idx).getId();
    }

    public void addTour(String name) {
        tourPlannerClient.putTour(
                DetailedTour.builder()
                        .name(name)
                        .build()
        );
        updateTours();
    }

    public void deleteTour(int idx) {
        tourPlannerClient.deleteTour(idx);
        updateTours();
    }

    @SneakyThrows
    public void updateTours() {
        toursList = new ArrayList<>();
        List<String> tourNamesList = new ArrayList<>();

        var tours = tourPlannerClient.getTours();
        for (var tour : tours) {
            if (toursFilter.isEmpty() || toursFilter.contains(Integer.toString(tour.getId()))) {
                tourNamesList.add(tour.getName());
                toursList.add(tour);
            }
        }

        tourNamesProperty.set(FXCollections.observableArrayList(tourNamesList));
    }
}
