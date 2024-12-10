package com.example.sortingalgorithmtesting;

import com.example.sortingalgorithmtesting.Models.AlgorithmModel;
import com.example.sortingalgorithmtesting.Utils.AlertMessage;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private ChoiceBox<String> AlgorithmChoiceBox;

    @FXML
    private TableView<String> DataTable;
    @FXML
    private TableColumn<String, String> UnsortedDataColumn;
    @FXML
    private TableColumn<String, String> SortedDataColumn;

    @FXML
    private TableView<AlgorithmModel> AlgorithmTable;
    @FXML
    private TableColumn<AlgorithmModel, String> AlgorithmColumn;
    @FXML
    private TableColumn<AlgorithmModel, Double> TimeColumn;
    @FXML
    private TableColumn<AlgorithmModel, Double> MemoryColumn;



    private final String[] algorithmList = {"Bubble sort", "Merge sort", "Quick sort", "Insertion sort"};
    private final ObservableList<String> dataList = FXCollections.observableArrayList();
    private final ObservableList<AlgorithmModel> modelList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AlgorithmChoiceBox.getItems().addAll(algorithmList);
        AlgorithmTable.setItems(modelList);
    }



    @FXML
    protected void SelectFileHandler() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select data ");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("text files", "*.csv"));
        File file = fileChooser.showOpenDialog(DataTable.getScene().getWindow());

        if(file!=null){
            dataList.clear();
            if(file.length() == 0){
                AlertMessage.showAlert(Alert.AlertType.WARNING, "Empty file", "WARNING", "Selected file is empty");
                return;
            }

            try(BufferedReader reader = new BufferedReader(new FileReader(file))){
                String line;

                while((line = reader.readLine()) != null){
                    dataList.add(line);
                }

            }catch(Exception e){
                AlertMessage.showAlert(Alert.AlertType.ERROR, "Unable to read file", "ERROR", "Unable to read file");
                throw new RuntimeException(e);
            }
        }
        DataTable.setItems(dataList);
        UnsortedDataColumn.setCellValueFactory(data -> {
            return new SimpleStringProperty(data.getValue());
        });


    }
    @FXML
    protected void ProgramHandler(){
        if(AlgorithmChoiceBox.getValue()!=null){

            AlgorithmModel model = new AlgorithmModel(AlgorithmChoiceBox.getValue(), dataList);

            long startTime = System.nanoTime();
            long beforeMemory = getUsedMemory();
            switch (AlgorithmChoiceBox.getValue()){
                case "Bubble sort":
                    model.BubbleSort();
                    break;
                case "Merge sort":
                    model.MergeSort();
                    break;
                case "Quick sort":
                    model.QuickSort();
                    break;
                case "Insertion sort":
                    model.InsertionSort();
                    break;
            }
            long endTime = System.nanoTime();
            long afterMemory = getUsedMemory();
            model.setAlgorithmTime((endTime-startTime)/1_000_000_000.0);
            model.setAlgorithmMemory((beforeMemory - afterMemory) / (1024.0));

            modelList.add(model);
            System.out.println(model.getList().toString());



            UnsortedDataColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
            SortedDataColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
            DataTable.setItems(model.getInputList());
            SortedDataColumn.getTableView().setItems(model.getList());

            System.out.println(model.getInputList().toString());

            AlgorithmColumn.setCellValueFactory(data -> data.getValue().algorithmNameProperty());
            TimeColumn.setCellValueFactory(data -> data.getValue().algorithmTimeProperty().asObject());
            MemoryColumn.setCellValueFactory(data -> data.getValue().algorithmMemoryProperty().asObject());



        } else {
            AlertMessage.showAlert(Alert.AlertType.ERROR, "No algorithm chosen", "Error", "No algorithm chosen");
            return;
        }

    }

    private static long getUsedMemory() {
        Runtime runtime = Runtime.getRuntime();
        runtime.gc(); // Optional: Suggest garbage collection for more accurate reading
        return runtime.totalMemory() - runtime.freeMemory();
    }
}