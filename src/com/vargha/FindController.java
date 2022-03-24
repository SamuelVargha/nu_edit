package com.vargha;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.*;

public class FindController implements Initializable {

    private TextArea textArea;

    private boolean check = false;

    private int position = 0;

    private Model model;

    @FXML
    private Button findButton;

    @FXML
    private TextField findField;

    @FXML
    private CheckBox matchCase;

    @FXML
    private RadioButton up;

    @FXML
    private RadioButton down;


    FindController(Model model, TextArea textArea) {
        this.textArea = textArea;
        this.model = model;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        findButton.setDisable(true);
        textArea.getText();
        ToggleGroup toggleGroup = new ToggleGroup();
        up.setToggleGroup(toggleGroup);
        down.setToggleGroup(toggleGroup);
        matchCase.setSelected(true);
        down.setSelected(true);

        findField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                findButton.setDisable(false);
                check = false;
                position = 0;
                if((event.getCode().equals(KeyCode.BACK_SPACE) ||event.getCode().equals(KeyCode.DELETE)) &&
                        (findField.getText().length() == 1 || findField.getText().length() == 0)){
                    findButton.setDisable(true);
                }

            }
        });
    }

    @FXML
    private void findNext(){
        System.out.println("Firstpos = " + position);
        List<Character> areaLetters = new ArrayList<>();
        List<Character> fieldLetters = new ArrayList<>();
        List<Character> chars = new ArrayList<>();

        for (int i = 0; i < textArea.getText().length(); i++){
            char c = textArea.getText().charAt(i);
            if(!matchCase.isSelected()){
                c = Character.toLowerCase(c);
            }
            areaLetters.add(c);
        }
        for (int i = 0; i < findField.getText().length(); i++){
            char c = findField.getText().charAt(i);
            if(!matchCase.isSelected()){
                c = Character.toLowerCase(c);
            }
            fieldLetters.add(c);
        }

        if(!check){
            int temp;
            if(up.isSelected()){
                temp = findNextWordUp(areaLetters, fieldLetters, chars, 0, areaLetters.size());
            } else {
                temp = findNextWord(areaLetters, fieldLetters, chars, 0);
            }
            if(temp == -1){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Word not found");
                alert.setContentText("Word not found");
                alert.showAndWait();
            } else {
                position = temp;
                System.out.println(position);
            }
            this.check = true;
        } else if (position != 0){
            int temp;
            if(up.isSelected()){
                temp = findNextWordUp(areaLetters, fieldLetters, chars, 0, position);
            } else {
                temp = findNextWord(areaLetters, fieldLetters, chars,  position);
            }

            if(temp == -1){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Word not found");
                alert.setContentText("Word not found");
                alert.showAndWait();
                textArea.selectRange(0,0);
                position = 0;
                check = false;
            } else {
                position = temp;
                System.out.println(position);
            }
            this.check = true;
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Word not found");
            alert.setContentText("Word not found");
            alert.showAndWait();
            textArea.selectRange(0,0);
            position = 0;
            check = false;
        }
    }

    @FXML
    private void keyTyped(){
        check = false;
        position = 0;
    }

    private boolean checkIfSame(List<Character> chars){
        Character[] tempArray = chars.toArray(new Character[0]);
        String word = toString(tempArray);
        if(matchCase.isSelected()){
            if(word.equals(findField.getText())){
                return true;
            }
        } else {
            if(word.equals(findField.getText().toLowerCase())){
                return true;
            }
        }
        textArea.selectRange(0,0);
        return false;
    }

    @FXML
    private void cancel(){
        check = false;
        position = 0;
        model.secondStageCancel();
    }

    private  String toString(Object[] a) {
        if (a == null)
            return "null";

        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        for (int i = 0; ; i++) {
            b.append(String.valueOf(a[i]));
            if (i == iMax)
                return b.toString();
        }
    }

    private int findNextWord(List<Character> areaLetters, List<Character> fieldLetters, List<Character> chars,  int position){
        if(findField.getText().length() < 2){
            for(int i = position;  i < areaLetters.size(); i++){
                if (areaLetters.get(i) == fieldLetters.get(0)){
                    chars.add(fieldLetters.get(0));
                    if (checkIfSame(chars)){
                        textArea.selectRange(i, i + 1);
                        return i + 1;
                    }
                }
            }
            return -1;
        }

        for(int i = position;  i < areaLetters.size(); i++){
            if(areaLetters.get(i) == fieldLetters.get(0)){

                int temp = i;
                for (int e = 0; e < fieldLetters.size(); e++){
                    chars.clear();
                    for(int u = i; u <= e + i; u++){
                        chars.add(areaLetters.get(u));
                    }
                    if(areaLetters.get(temp) == fieldLetters.get(e)){
                        textArea.selectRange(i, i + e + 1);
                        chars.clear();
                        for(int u = i; u <= e + i; u++){
                            chars.add(areaLetters.get(u));
                        }
                        if (checkIfSame(chars)){
                            return i + 1;
                        }

                        try {
                            if(areaLetters.get(temp + 1) != fieldLetters.get(e + 1)){
                                textArea.selectRange(0, 0);
                                break;
                            }
                            temp++;
                        }catch (IndexOutOfBoundsException exception){
                            textArea.selectRange(0, 0);
                            break;
                        }
                    }
                }
            }
        }
        return -1;
    }

    private int findNextWordUp(List<Character> areaLetters, List<Character> fieldLetters, List<Character> chars, int fieldInt, int position){
        if(findField.getText().length() < 2){
            for(int i = areaLetters.size() - (areaLetters.size() - position) - 1;  i > 0; i--){
                if (areaLetters.get(i) == fieldLetters.get(0)){
                    chars.add(fieldLetters.get(0));
                    textArea.selectRange(i, i + 1);
                    return i - 1;
                }
            }
            return -1;
        }

        for(int i = areaLetters.size() - (areaLetters.size() - position) - 1;  i > 0; i--){
            int count = 0;
            chars.clear();
            System.out.println("i = + " + i);
            if(areaLetters.get(i) == fieldLetters.get(fieldLetters.size() - 1)){
                int temp = i;
                for(int e = fieldLetters.size() - 1; e > -1; e--){
                    count++;
                    if(fieldLetters.get(e) == areaLetters.get(temp)){
                        chars.add(areaLetters.get(temp));
                        System.out.println(areaLetters.get(temp));
                        Collections.reverse(chars);
                        if (checkIfSame(chars)){
                            System.out.println("ayyy = " + i);
                            textArea.selectRange(i + 1, i - count + 1);
                            return i - 1;
                        }
                        Collections.reverse(chars);
                        temp--;
                    }else {
                        break;
                    }
                }
            }
        }
        return -1;
    }



}
