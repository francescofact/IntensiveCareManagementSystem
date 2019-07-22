package therapy.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import therapy.Datastore;
import therapy.Patient;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class PatientAllListController implements Initializable {
    @FXML private TableView<Patient> patientsList;
    @FXML private TableColumn<Patient, String> colFullName;
    @FXML private TableColumn<Patient, String> colCod;
    @FXML private TableColumn<Patient, String> colBirth;
    @FXML private TableColumn<Patient, String> colCity;
    @FXML private TableColumn<Patient, String> colHospitalized;
    @FXML private TextField textSearch;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<Patient> patients = Datastore.getPatients();
        colFullName.setCellValueFactory(new PropertyValueFactory<>("FullName"));
        colCod.setCellValueFactory(new PropertyValueFactory<>("CodFis"));
        colBirth.setCellValueFactory(new PropertyValueFactory<>("BirthDate"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("BirthTown"));
        colHospitalized.setCellValueFactory(new PropertyValueFactory<>("Hospitalization"));

        if (!patients.isEmpty()){
            ObservableList<Patient> data = FXCollections.observableArrayList(Datastore.getPatients());
            patientsList.setItems(data);
            patientsList.getSelectionModel().selectFirst();
        }

        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        ObservableList<Patient> obpatients = FXCollections.observableArrayList(patients);
        FilteredList<Patient> filteredData = new FilteredList<>(obpatients, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        textSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (person.getFullName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (person.getCodFis().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Patient> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(patientsList.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        patientsList.setItems(sortedData);
    }

    @FXML
    void handleClick(MouseEvent event) {
        if (event.getClickCount() == 2 ){
             openPatient(patientsList.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    private void handleSearch(){
        String text = textSearch.getText();
        Boolean found = false;
        for (Patient patient: patientsList.getItems()){
            if (patient.getFullName().contains(text) || patient.getCodFis().contains(text)){
                found = true;
                //TODO: finisci
            }
        }
    }

    void openPatient(Patient patient) {
        if (Datastore.allLoaders.containsKey("patientPage" + patient.getCodFis())){
            GUI.showDialog(Alert.AlertType.WARNING, "Paziente", "La scheramata relativa a questo paziente è già aperta");
        } else {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../gui/patientPage.fxml"));
                Parent root1 = fxmlLoader.load();
                PatientPageController controller = fxmlLoader.<PatientPageController>getController();
                controller.loadPatient(patient);

                Stage stage = new Stage();
                stage.setTitle("Paziente");
                stage.setScene(new Scene(root1));
                Datastore.allLoaders.put("patientPage" + patient.getCodFis(), fxmlLoader);
                stage.setOnCloseRequest((WindowEvent event1) -> {
                    Datastore.allLoaders.remove("patientPage" + patient.getCodFis());
                });
                stage.show();
            } catch (IOException e) {
                GUI.showDialog(Alert.AlertType.ERROR, "Error", "Momentaneamente non è possibile aprire la pagina del paziente");
            }
        }
    }
}