package at.fhtw.viewModel;

import at.fhtw.client.TourPlannerClient;
import at.fhtw.client.model.DetailedTour;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import lombok.Getter;

import java.io.ByteArrayInputStream;
import java.util.Base64;

public class TourDetailViewModel {

    private static TourDetailViewModel instance;
    @Getter
    private final StringProperty nameField = new SimpleStringProperty();
    @Getter
    private final StringProperty totalDistanceLabel = new SimpleStringProperty();
    @Getter
    private final StringProperty descriptionField = new SimpleStringProperty();
    private final TourPlannerClient tourPlannerClient;

    @Getter
    private final ObjectProperty<Image> imageProperty = new SimpleObjectProperty<Image>();

    @Getter
    private DetailedTour currentTour;

    public TourDetailViewModel(TourPlannerClient tourPlannerClient) {
        this.tourPlannerClient = tourPlannerClient;
    }

    public void updateCurrentTour() {
        setTour(currentTour.getId());
    }

    public void setTour(int id) {
        if (currentTour != null) {
            updateTourName();
            updateTourDescription();
        }

        var optionalTour = tourPlannerClient.getTour(id);
        if (optionalTour.isPresent()) {
            currentTour = optionalTour.get();
            nameField.setValue(currentTour.getName());
            totalDistanceLabel.setValue(Float.toString(currentTour.getDistance()));
            descriptionField.set(currentTour.getDescription());
            updateImage();
        }
    }

    public void updateTourName() {
        var newName = nameField.getValue();
        if (!newName.equals(currentTour.getName())) {
            currentTour.setName(newName);
            tourPlannerClient.putTour(currentTour);
        }
    }

    public void updateTourDescription() {
        var newDescription = descriptionField.getValue();
        if (!newDescription.equals(currentTour.getDescription())) {
            currentTour.setDescription(newDescription);
            tourPlannerClient.putTour(currentTour);
        }
    }

    private void updateImage() {
        if (currentTour.getImage().length() != 0) {
            byte[] imageBytes = Base64.getDecoder().decode(currentTour.getImage());
            Image image = new Image(new ByteArrayInputStream(imageBytes));
            imageProperty.setValue(image);
        } else {
            Image image = new Image(new ByteArrayInputStream(new byte[0]));
            imageProperty.setValue(image);
        }
    }
}
