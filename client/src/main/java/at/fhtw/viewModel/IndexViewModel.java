package at.fhtw.viewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;

public class IndexViewModel {

    @Getter
    StringProperty topTextProperty = new SimpleStringProperty("Hello World!");

    public void changeTextClicked() {
        topTextProperty.setValue("Bye world!");
    }
}
