package com.vargha;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.util.ResourceBundle;


public class AboutController implements Initializable {

    private Model model;

    @FXML
    private ImageView imageView;

    @Override
    public void initialize (URL location, ResourceBundle resources) {
        Image image = new Image("file:icon.png");
        imageView.setImage(image);
    }

    AboutController(Model model) {
        this.model = model;
    }

    @FXML
    private void ok(){
        model.secondStageCancel();
    }
}
