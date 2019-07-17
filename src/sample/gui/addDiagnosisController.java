package sample.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import sample.Datastore;
import sample.Patient;

public class addDiagnosisController {
    Patient currentPatient = null;
    @FXML private TextArea textarea;


    @FXML
    void saveDiagnosis(ActionEvent event) {
        if (textarea.getText().isEmpty()) {
            GUI.showDialog(Alert.AlertType.WARNING, "Warning", "Per salvare è necessario inserire una diagnosi");
            return;
        }

        currentPatient.setDiagnosis(textarea.getText());
        Datastore.write();
        GUI.showDialog(Alert.AlertType.INFORMATION, "Info", "Diagnosi iniziale aggiornata!");

        Stage stage = (Stage)((Node)event.getTarget()).getScene().getWindow();
        stage.close();
    }


    public void setCurrentPatient(Patient currentPatient){
        this.currentPatient = currentPatient;
        textarea.setText(currentPatient.getDiagnosis());
    }
}