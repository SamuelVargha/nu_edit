package com.vargha;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import java.net.URL;
import java.util.*;

public class Controller implements Initializable {

    private Model model;
    private int wrapCheck;
    @FXML
    private TextArea textArea;

    @FXML
    private Label wordCount;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.model = new Model(textArea);

    }

    @FXML
    private void newFile() {
        model.newFile(textArea);
    }


    @FXML
    private void wrapText() {
        if (wrapCheck == 1) {
            textArea.setWrapText(false);
            wrapCheck = 0;
        } else {
            textArea.setWrapText(true);
            wrapCheck = 1;
        }

    }

    @FXML
    private void keyTyped() {
        List<String> lineList = new LinkedList<>(Arrays.asList(textArea.getText().split("\n")));
        int words = 0;

        for(int i = 0; i < lineList.size(); i++){
            if(lineList.get(i).equals("") || lineList.get(i).equals(" ") || lineList.get(i).equals("\n")){
                continue;
            }

            List<String> wordList = new LinkedList<>(Arrays.asList(lineList.get(i).split(" ")));

            words += wordList.size();
        }

        if(words < 0){
            words = 0;
        }

        if(words == 1){
            wordCount.setText(words + " word");
        } else {
            wordCount.setText(words + " words");
        }

    }

    @FXML
    private void font() {
        model.font();
    }

    @FXML
    private void saveAs() {
        model.saveAs(textArea);
    }

    @FXML
    public void save() {
        model.save(textArea);
    }

    @FXML
    public void open() {
        model.open(textArea);
    }

    @FXML
    public void undo() {
        model.undo(textArea);
    }

    @FXML
    public void redo() {
        model.redo(textArea);
    }

    @FXML
    private void cut() {
        model.cut(textArea);
    }

    @FXML
    public void copy() {
        model.copy(textArea);
    }

    @FXML
    public void paste() {
        model.paste(textArea);
    }

    @FXML
    public void delete() {
        model.delete(textArea);

    }

    @FXML
    private void selectAll() {
        model.selectAll(textArea);
    }

    @FXML
    private void find() {
        model.find();
    }

    @FXML
    private void replace() {
        model.replace();
    }

    @FXML
    private void timeDate() {
        model.timeDate();
    }

    @FXML
    public void close() {
        model.close(textArea);
    }

    @FXML
    public void about() {
        model.about();
    }

}
