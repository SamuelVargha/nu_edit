package com.vargha;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

class Model {
    private TextFile currentFile;
    private TextArea textArea;
    private FontController fontController;
    private FindController findController;
    private ReplaceController replaceController;
    private AboutController aboutController;
    private Stage secondStage = new Stage();

     Model(TextArea textArea) {
        List<String> temp = new ArrayList<>();
        currentFile = new TextFile(null, temp);
        this.textArea = textArea;
        fontController = new FontController(this, textArea.getFont());
        findController = new FindController(this, textArea);
        replaceController = new ReplaceController(this, textArea);
        aboutController = new AboutController(this);

    }

     void newFile(TextArea textArea) {

        if(!checkIfSaved(textArea)){
            return;
        }
        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
        infoAlert.setTitle("Directory");
        infoAlert.setHeaderText("Choose a directory for your new file");
        infoAlert.setContentText("");
        infoAlert.showAndWait();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Documents"));
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle("Choose a directory");
        File file = fileChooser.showSaveDialog(null);
        Path path;

        try {
            path = file.toPath();
        } catch (NullPointerException e){
            return;
        }

        TextFile textFile = new TextFile(path, Arrays.asList("", ""));
        currentFile = textFile;

        try {
            Files.write(path, textFile.getContent(), StandardOpenOption.CREATE);
            textArea.clear();

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.CLOSE);
            alert.setTitle("Error");
            alert.show();
        }

    }

     void saveAs(TextArea textArea) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Documents"));
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle("Choose a directory");
        File file = fileChooser.showSaveDialog(null);
        if(file == null){
            return;
        }

        Path path = file.toPath();
        List<String> list = new ArrayList<>();

        try {
            for (String line : textArea.getText().split("\n")) {
                System.out.println();
                list.add(line);
            }


        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.CLOSE);
            alert.setTitle("Error");
            alert.show();
        }
        TextFile textFile = new TextFile(path, list);

        try {
            Files.write(path, textFile.getContent(), StandardOpenOption.CREATE);

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.CLOSE);
            alert.setTitle("Error");
            alert.show();
        }

        currentFile = textFile;

    }

     void save(TextArea textArea) {
        try {
            currentFile.getPath();
            if(currentFile.getPath() == null){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Please choose a directory", ButtonType.OK);
                alert.showAndWait();
                saveAs(textArea);
                return;
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Please choose a directory", ButtonType.OK);
            alert.showAndWait();
            saveAs(textArea);
            return;
        }

        Path path = currentFile.getPath();
        List<String> list = new ArrayList<>();

        try {
            for (String line : textArea.getText().split("\n")) {
                System.out.println(line);
                list.add(line);
            }

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.CLOSE);
            alert.setTitle("Error");
            alert.show();
        }

        TextFile textFile = new TextFile(path, list);

        try {
            Files.write(path, textFile.getContent());

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.CLOSE);
            alert.setTitle("Error");
            alert.show();
        }

        currentFile = textFile;

    }

     void open(TextArea textArea) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Documents"));
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle("Choose a file");
        File file = fileChooser.showOpenDialog(null);


        if(file != null){
            Path path = file.toPath();
            List<String> list = new ArrayList<>();

            try {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()){
                    list.add(scanner.nextLine());
                }
                System.out.println(list);
                scanner.close();
            } catch (FileNotFoundException e){
                Alert alert = new Alert(Alert.AlertType.ERROR, "File not found", ButtonType.CLOSE);
                alert.setTitle("Error");
                alert.show();
            }

            TextFile textFile = new TextFile(path, list);

            try {
                textArea.clear();
                list = textFile.getContent();
                for(String line: list){
                    textArea.appendText(line + "\n");
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.CLOSE);
                alert.setTitle("Error");
                alert.show();
            }
            currentFile = textFile;
        }
    }

     void undo(TextArea textArea) {
        textArea.undo();
    }


     void redo(TextArea textArea) {
        textArea.redo();
    }

    void cut(TextArea textArea){
         textArea.cut();
    }


     void copy(TextArea textArea) {
        textArea.copy();
    }


     void paste(TextArea textArea) {
        textArea.paste();
    }


     void delete(TextArea textArea) {
        textArea.deleteText(textArea.getSelection());
    }

    void selectAll(TextArea textArea){
         textArea.selectAll();
    }

    void find(){
        FXMLLoader newLoader = new FXMLLoader(getClass().getResource("findUI.fxml"));
        newLoader.setControllerFactory(t -> findController);
        try {
            secondStage.setTitle("Find");
            Parent root = newLoader.load();
            secondStage.setScene(new Scene(root, 350, 120));
            secondStage.setResizable(false);
            secondStage.getIcons().add(new Image("file:icon.png"));
            secondStage.show();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

     void close(TextArea textArea){
         if(checkIfSaved(textArea)){
             Platform.exit();
         }
    }

    void replace(){
        FXMLLoader newLoader = new FXMLLoader(getClass().getResource("replaceUI.fxml"));
        newLoader.setControllerFactory(t -> replaceController);
        try {
            secondStage.setTitle("Replace");
            Parent root = newLoader.load();
            secondStage.setScene(new Scene(root, 350, 160));
            secondStage.setResizable(false);
            secondStage.getIcons().add(new Image("file:icon.png"));
            secondStage.show();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    void timeDate(){
       textArea.insertText(textArea.getCaretPosition(), LocalDateTime.now().getHour() + ":"
               + LocalDateTime.now().getMinute() + " " + LocalDateTime.now().getDayOfMonth() + "/"
               + LocalDateTime.now().getMonthValue() + "/" + LocalDateTime.now().getYear());
    }

    void font(){
        FXMLLoader newLoader = new FXMLLoader(getClass().getResource("fontUI.fxml"));
        newLoader.setControllerFactory(t -> fontController);
        try {
            secondStage.setTitle("Choose a font");
            Parent root = newLoader.load();
            secondStage.setScene(new Scene(root, 520, 430));
            secondStage.getIcons().add(new Image("file:icon.png"));
            secondStage.show();

        }catch (IOException e){
            e.printStackTrace();
        }

    }

    void fontOk(Font font){
         textArea.setFont(font);
         secondStage.close();
    }

    void about(){
        FXMLLoader newLoader = new FXMLLoader(getClass().getResource("aboutUI.fxml"));
        newLoader.setControllerFactory(t -> aboutController);
        try {
            secondStage.setTitle("About");
            Parent root = newLoader.load();
            secondStage.setScene(new Scene(root, 385, 250));
            secondStage.setResizable(false);
            secondStage.show();
            secondStage.getIcons().add(new Image("file:icon.png"));
            secondStage.setAlwaysOnTop(true);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    void secondStageCancel(){
        secondStage.close();
    }

     private boolean checkIfSaved(TextArea textArea){
        List<String> list = new ArrayList<>();
        if(textArea.getText().length() == 0){
            return true;
        }
        for (String line : textArea.getText().split("\n")) {
            if(line.equals("")){

            }
            list.add(line);
        }
        System.out.println(list);
        System.out.println(currentFile.getContent());

        if(list.get(0).equals("") && list.size() < 2){
            currentFile.getContent().add("");
        }

        if(!currentFile.getContent().equals(list)){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Would you like to save first?");
            alert.setContentText("");

            ButtonType buttonSave = new ButtonType("Save");
            ButtonType buttonDoNotSave = new ButtonType("Don't save");
            ButtonType buttonCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonSave, buttonDoNotSave, buttonCancel);
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == buttonSave){
                save(textArea);
            } else if(result.get() == buttonCancel){
                return false;
            } else if(result.get() == buttonDoNotSave){
                System.out.println("Continuing");
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("An error occurred");
                errorAlert.showAndWait();
            }
        }
        return true;
    }




 }
