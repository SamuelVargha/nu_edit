package com.vargha;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FontController implements Initializable {

    private Font font;
    private Model model;


    FontController(Model model, Font font) {
        this.model = model;
        this.font = font;
    }

    @FXML
    private ListView<String> fontList;

    @FXML
    private TextField sizeField;

    @FXML
    private Text sampleText;

    @FXML
    ListView<String> styleList;

    @FXML
    ListView<Integer> sizeList;

    @FXML
    private void keyTyped(){
        sizeField.textProperty().addListener((observable, oldValue, newValue) -> {
            if ((newValue.length() > 5))sizeField.setText(oldValue);
        });

        sizeField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.matches("\\d*")){
                    sizeField.setText(newValue.replaceAll("[^\\d]", ""));
                }else if(!sizeField.getText().equals("")){
                    if(Integer.parseInt(sizeField.getText()) <= 72){
                            changeFont();
                    }
                }
            }
        });
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> systemList = Font.getFamilies();
        ObservableList<String> systemObservList = FXCollections.observableArrayList(systemList);
        fontList.setItems(systemObservList);
        fontList.getSelectionModel().select(font.getName());
        sizeField.setEditable(false);
        sizeList.getSelectionModel().select((int) font.getSize());
        sampleText.fontProperty();
    }

    @FXML
    private void changeFont(){
        sizeField.setEditable(true);
        sizeList.getItems().clear();
        styleList.getItems().clear();
        sizeList.getItems().addAll(8,9,10,11,12,14,16,18,20,22,24,26,28,36,48,72);
        styleList.getItems().addAll("Bold", "Bold Italic", "Extra Bold", "Italic", "Light");

        whichFont();
    }

    @FXML
    private void changeStyle(){
        whichFont();
    }

    @FXML
    private void changeSize(){
        whichFont();
    }

    @FXML
    private void ok(){
        model.fontOk(font);
    }

    @FXML
    private void cancel(){
        model.secondStageCancel();
    }

    private void whichFont(){
        String selectedFamily;
        String selectedStyle;
        int selectedSize;

        selectedFamily = fontList.getSelectionModel().getSelectedItem();
        selectedStyle = styleList.getSelectionModel().getSelectedItem();

        selectedSize = 0;

        try {
            if(sizeField.getText().length() == 0){
                selectedSize = sizeList.getSelectionModel().getSelectedItem();
            } else {
                selectedSize = Integer.parseInt(sizeField.getText());
            }
        } catch (NullPointerException e){
            System.out.println();
        }

        if(selectedSize == 0){
            selectedSize = 16;
        }

        if(selectedStyle != null){
            if(selectedStyle.equals("Bold")){
                font = Font.font(selectedFamily, FontWeight.BOLD, selectedSize);
            } else if(selectedStyle.equals("Bold Italic")){
                font = Font.font(selectedFamily, FontWeight.BOLD, FontPosture.ITALIC, selectedSize);
            }else if(selectedStyle.equals("Extra Bold")){
                font = Font.font(selectedFamily, FontWeight.EXTRA_BOLD, selectedSize);
            } else if(selectedStyle.equals("Italic")){
                font = Font.font(selectedFamily, FontPosture.ITALIC, selectedSize);
            } else if(selectedStyle.equals("Light")){
                font = Font.font(selectedFamily, FontWeight.EXTRA_LIGHT, selectedSize);
            } else {
                font = Font.font(selectedFamily, FontPosture.REGULAR, selectedSize);
            }
        } else {
            font = Font.font(selectedFamily, selectedSize);
        }

        sampleText.setFont(font);
    }







}
