package at.fhtw.view;

import at.fhtw.viewModel.IndexViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IndexView {
    @FXML
    public Text topText;
    @FXML
    public Button changeTextButton;

    IndexViewModel indexViewModel;

    @FXML
    public void initialize() {
        indexViewModel = new IndexViewModel();

        topText.textProperty().bindBidirectional(indexViewModel.getTopTextProperty());
    }

    @FXML
    public void changeTextClicked(ActionEvent actionEvent) {
        indexViewModel.changeTextClicked();
    }
}
