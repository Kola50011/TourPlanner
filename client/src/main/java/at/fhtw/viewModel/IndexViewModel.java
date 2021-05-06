package at.fhtw.viewModel;

import at.fhtw.client.TourPlannerClient;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.SneakyThrows;

public class IndexViewModel {

    @Getter
    StringProperty topTextProperty = new SimpleStringProperty("Hello World!");

    @Getter
    ObservableList<String> tourList = FXCollections.observableArrayList();

    @SneakyThrows
    public IndexViewModel() {
        var client = new TourPlannerClient();
        var tours = client.getTours();
        for (var tour : tours) {
            tourList.add(tour.getName());
        }
    }

    public void changeTextClicked() {
        topTextProperty.setValue("Bye world!");
    }

}
