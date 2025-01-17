package therapy.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import therapy.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class PatientPageController implements Initializable {
    Patient currentPatient = null;
    Stage thisStage;

    @FXML private Label labelName;
    @FXML private Label labelBirthTown;
    @FXML private Label labelCodFis;
    @FXML private Label labelBirthDate;
    @FXML private LineChart<String, Number> chartPressure;
    @FXML private LineChart<String, Number> chartHeartBeat;
    @FXML private LineChart<String, Number> chartTemperature;
    @FXML private ImageView imageUser;
    @FXML private Button buttonDiagnosis;
    @FXML private Button buttonPrescription;
    @FXML private Button buttonAdministration;
    @FXML private Button buttonReport;
    @FXML private Button buttonDischarge;
    @FXML private GridPane gridCharts;
    @FXML private Label labelLetter;
    @FXML private VBox vboxDischarged;
    @FXML private Menu menuFile;

    @FXML
    void handleAdministrationsList(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = openPopupWindow("Lista Somministrazioni", "../gui/administrationsList.fxml", event);
            AdministrationsListController controller = fxmlLoader.<AdministrationsListController>getController();
            controller.setCurrentPatient(currentPatient);
        } catch (IOException e) {
            GUI.showDialog(Alert.AlertType.ERROR, "Error", "Momentaneamente impossibile aprire lista somministrazioni");
        }
    }

    @FXML
    void handleAddDiagnosis(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = openPopupWindow("Aggiungi Diagnosi", "../gui/addDiagnosis.fxml", event);
            AddDiagnosisController controller = fxmlLoader.<AddDiagnosisController>getController();
            controller.setCurrentPatient(currentPatient);
        } catch (IOException e) {
            GUI.showDialog(Alert.AlertType.ERROR, "Error", "Momentaneamente impossibile aggiungere una diagnosi");
        }
    }

    @FXML
    void handlePrescriptionsList(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = openPopupWindow("Lista Prescrizioni", "../gui/prescriptionList.fxml", event);
            PrescriptionListController controller = fxmlLoader.<PrescriptionListController>getController();
            controller.setCurrentPatient(currentPatient);
        } catch(IOException e) {
            GUI.showDialog(Alert.AlertType.ERROR, "Error", "Momentaneamente impossibile aprire la lista delle prescrizioni");
        }
    }

    @FXML
    void handleGenerateReport(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = openPopupWindow("Genera Report", "../gui/reportAskDates.fxml", event);
            Window thiswindow = ((Node)event.getTarget()).getScene().getWindow();
            ReportAskDatesController controller = fxmlLoader.getController();
            controller.setCurrentPatient(currentPatient, thiswindow);
        } catch(IOException e) {
            GUI.showDialog(Alert.AlertType.ERROR, "Error", "Momentaneamente impossibile generare report");
        }
    }

    @FXML
    void handleDischarge(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../gui/addDischargeLetter.fxml"));
            Parent root = fxmlLoader.load();
            Window thisWindow = ((Node) event.getTarget()).getScene().getWindow();
            thisStage = (Stage) ((Node) event.getTarget()).getScene().getWindow();

            Stage dischargeStage = new Stage();
            dischargeStage.setTitle("Aggiungi lettera di dimissioni");
            dischargeStage.initModality(Modality.WINDOW_MODAL);
            dischargeStage.initOwner(thisWindow);
            dischargeStage.setScene(new Scene(root));
            dischargeStage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_HIDDEN, this::closeWindowEvent);
            dischargeStage.getIcons().add(new Image(getClass().getResourceAsStream("/imgs/icon.png")));
            dischargeStage.show();

            AddDischargeLetterController controller = fxmlLoader.<AddDischargeLetterController>getController();
            controller.setCurrentPatient(currentPatient);
        } catch (IOException e) {
            GUI.showDialog(Alert.AlertType.ERROR, "Error", "Momentaneamente impossibile dimettere paziente");
        }
    }

    private void closeWindowEvent(WindowEvent event) {
        if (!this.currentPatient.getDischargeLetter().isEmpty()) {
            showUnHospitalizedView();
            System.out.println("Patient discharged");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(Datastore.getActiveUser());
        User activeUser = Datastore.getActiveUser();
        System.out.println("Active User: " + activeUser.getClass().getSimpleName());

        Image image = new Image(getClass().getResourceAsStream("/imgs/user.png"));
        imageUser.setImage(image);

        enableButtons();
        chartHeartBeat.getXAxis().setAutoRanging(true);

    }


    public void loadPatient(Integer patientId){
        ArrayList<Patient> patients = Datastore.getPatients();
        currentPatient = patients.get(patientId);
        loadPatient(currentPatient);
    }

    public void loadPatient(Patient patient){
        this.currentPatient = patient;

        labelCodFis.setText(currentPatient.getCodFis());
        labelName.setText(currentPatient.getFullName());
        labelBirthDate.setText(currentPatient.getDate().toString());
        labelBirthTown.setText(currentPatient.getBirthTown());

        vboxDischarged.setVisible(false);
        if (!currentPatient.getHospitalization()) showUnHospitalizedView();
        //load charts and update them automaticaly updates charts every x time
        loadCharts();
        updateCharts();
    }

    private void loadCharts(){
        //HB chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("HB");
        for (HeartBeat beat: currentPatient.getHeartBeats()){
            if (beat.getTimestamp().after(new Date(System.currentTimeMillis() - 7200 * 1000)))
                series.getData().add(new XYChart.Data<>(beat.getTimestamp().toString(), beat.getHeartBeat()));
        }
        chartHeartBeat.getXAxis().setTickLabelsVisible(false);
        chartHeartBeat.getXAxis().setOpacity(0);
        chartHeartBeat.getStylesheets().add(getClass().getResource("/css/charts.css").toExternalForm());
        chartHeartBeat.getData().add(series);
        chartHeartBeat.setTitle("Battito");
        chartHeartBeat.setAnimated(false);

        //temp
        series = new XYChart.Series<>();
        series.setName("Temp");
        for (Temperature temp: currentPatient.getTemperatures()){
            if (temp.getTimestamp().after(new Date(System.currentTimeMillis() - 7200 * 1000)))
                series.getData().add(new XYChart.Data<>(temp.getTimestamp().toString(), temp.getTemperature()));

        }
        chartTemperature.getXAxis().setTickLabelsVisible(false);
        chartTemperature.getXAxis().setOpacity(0);
        chartTemperature.getStylesheets().add(getClass().getResource("/css/charts.css").toExternalForm());
        chartTemperature.getData().add(series);
        chartTemperature.setTitle("Temperatura");
        chartTemperature.setAnimated(false);

        //pressure
        series = new XYChart.Series<>();
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series.setName("Minima");
        series1.setName("Massima");
        for (Pressure press: currentPatient.getPressures()){
            if (press.getTimestamp().after(new Date(System.currentTimeMillis() - 7200 * 1000))) {
                series.getData().add(new XYChart.Data<>(press.getTimestamp().toString(), press.getPressMin()));
                series1.getData().add(new XYChart.Data<>(press.getTimestamp().toString(), press.getPressMax()));
            }
        }
        chartPressure.getXAxis().setTickLabelsVisible(false);
        chartPressure.getXAxis().setOpacity(0);
        chartPressure.getStylesheets().add(getClass().getResource("/css/charts.css").toExternalForm());
        chartPressure.getData().addAll(series, series1);
        chartPressure.setTitle("Pressione");
        chartPressure.setAnimated(false);
    }

    public void enableButtons(){
        switch (Datastore.getActiveUser().getUserType()) {
            case CHIEFDOCTOR: {
                buttonReport.setDisable(false);
                buttonDischarge.setDisable(false);
            }
            case DOCTOR: {
                buttonPrescription.setDisable(false);
                buttonDiagnosis.setDisable(false);
            }
            case NURSE: {
                buttonAdministration.setDisable(false);
            }
        }
    }

    @FXML
    void handleGenerateRand(ActionEvent event) {
        currentPatient.generateFakeData();
        GUI.showDialog(Alert.AlertType.INFORMATION, "Info", "Dati generati corretamente!");
        chartHeartBeat.getData().clear();
        chartPressure.getData().clear();
        chartTemperature.getData().clear();
        loadCharts();
        Datastore.write();
    }

    @FXML
    void handleClearClinicalData(ActionEvent event) {
        currentPatient.clearClinicalData();
        GUI.showDialog(Alert.AlertType.INFORMATION, "Info", "Dati cancellati!");
        //updating all other windows
        for(Map.Entry<String, FXMLLoader> entry: Datastore.allLoaders.entrySet()){
            switch (entry.getKey()){
                case "dashboard":{
                    HomeController controller = entry.getValue().getController();
                    controller.reset();
                    controller.loadList();
                    break;
                }
            }
        }

        chartHeartBeat.getData().clear();
        chartPressure.getData().clear();
        chartTemperature.getData().clear();
        loadCharts();

        Datastore.write();

    }

    FXMLLoader openPopupWindow(String title, String fxml, ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = fxmlLoader.load();

        Window thisWindow = ((Node)event.getTarget()).getScene().getWindow();
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(thisWindow);
        stage.setScene(new Scene(root));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/imgs/icon.png")));
        stage.show();

        return fxmlLoader;
    }

    private void showUnHospitalizedView(){
        gridCharts.setVisible(false);
        vboxDischarged.setVisible(true);
        buttonDiagnosis.setDisable(true);
        buttonDischarge.setDisable(true);
        menuFile.setDisable(true);
        labelLetter.setText(currentPatient.getDischargeLetter());
    }

    public void updateCharts() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(30), ev -> {
            System.out.println("Updating charts!");
            chartHeartBeat.getData().clear();
            chartPressure.getData().clear();
            chartTemperature.getData().clear();
            loadCharts();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
}
