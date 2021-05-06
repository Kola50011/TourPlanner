package at.fhtw.view;

import at.fhtw.viewModel.IndexViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IndexController {
//    @FXML
//    public Text topText;
//
//    @FXML
//    public Button changeTextButton;

    @FXML
    public ListView tourList;

    IndexViewModel indexViewModel;

    @FXML
    public void initialize() {
        indexViewModel = new IndexViewModel();

//        topText.textProperty().bindBidirectional(indexViewModel.getTopTextProperty());
        tourList.setItems(indexViewModel.getTourList());
    }

    @FXML
    public void changeTextClicked(ActionEvent actionEvent) {
        indexViewModel.changeTextClicked();
    }
}
