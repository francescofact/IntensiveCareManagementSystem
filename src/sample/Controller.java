package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.chart.LineChart;
import javafx.event.ActionEvent;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class Controller {

    @FXML
    private LineChart<?, ?> grafico1;

    @FXML
    private LineChart<?, ?> grafico11;

    @FXML
    private LineChart<?, ?> grafico111;

    @FXML
    void handleAddValue(ActionEvent event) {
        //pressione max value is 8
        //addDataTo(grafico1,  20, 0);
        //addDataTo(grafico1,  55, 1);
        addDataTo(grafico11, 55, 0);

    }

    @FXML
    void handleLogin(ActionEvent event) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sample/login.fxml"));
        Parent root1 = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Login");
        stage.setScene(new Scene(root1));
        stage.show();

    }

    @FXML
    void handleAbout(ActionEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText(null);
        alert.setContentText("Ciao!\nSoftware sviluppato da\nFrancesco Fattori, Giacomo Frigo e Alex Pasquali");

        alert.showAndWait();
    }


    //method to help
    @SuppressWarnings("unchecked")
    private static void addDataTo(LineChart linechart, Integer y, Integer serieIndex){
        XYChart.Series currentSeries = (XYChart.Series) linechart.getData().get(serieIndex);
        //delete first data
        currentSeries.getData().remove(0);
        int numOfPoint = currentSeries.getData().size();

        for(int i=0; i<numOfPoint; i++) {
            //reduce XValue
            XYChart.Data<Number, Number> data = (XYChart.Data<Number, Number>)currentSeries.getData().get(i);
            int x = (int) data.getXValue();
            System.out.println("X Value: " + x);
            data.setXValue(x - 1);
        }
        currentSeries.getData().add(new XYChart.Data<>(numOfPoint, y));
    }

}