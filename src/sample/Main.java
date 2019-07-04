package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

public class Main extends Application {
    private Parent MainRoot;
    @Override
    @SuppressWarnings("unchecked")
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
        MainRoot = root;
        primaryStage.setTitle("Dashboard");
        primaryStage.setScene(new Scene(root, 1000, 500));
        primaryStage.show();

        LineChart lineChart = (LineChart) root.lookup("#grafico1");

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Minima");

        series1.getData().add(new XYChart.Data<>(0, 20));
        series1.getData().add(new XYChart.Data<>(1, 23));
        series1.getData().add(new XYChart.Data<>(2, 14));
        series1.getData().add(new XYChart.Data<>(3, 15));
        series1.getData().add(new XYChart.Data<>(4, 24));
        series1.getData().add(new XYChart.Data<>(5, 23));
        series1.getData().add(new XYChart.Data<>(6, 14));
        series1.getData().add(new XYChart.Data<>(7, 15));

        XYChart.Series series2 = new XYChart.Series();
        series2.setName("Massima");
        series2.getData().add(new XYChart.Data<>(0, 44));
        series2.getData().add(new XYChart.Data<>(1, 33));
        series2.getData().add(new XYChart.Data<>(2, 34));
        series2.getData().add(new XYChart.Data<>(3, 25));
        series2.getData().add(new XYChart.Data<>(4, 44));
        series2.getData().add(new XYChart.Data<>(5, 33));
        series2.getData().add(new XYChart.Data<>(6, 34));
        series2.getData().add(new XYChart.Data<>(7, 25));


        lineChart.getData().addAll(series1, series2);

        //heartbeat
        LineChart lineChart2 = (LineChart) root.lookup("#grafico11");
        XYChart.Series series3 = new XYChart.Series();
        series3.setName("HeartBeat");
        series3.getData().add(new XYChart.Data<>(0,30));
        series3.getData().add(new XYChart.Data<>(1,31));
        series3.getData().add(new XYChart.Data<>(2,32));
        series3.getData().add(new XYChart.Data<>(3,33));
        series3.getData().add(new XYChart.Data<>(4,34));
        series3.getData().add(new XYChart.Data<>(5,35));


        lineChart2.getData().add(series3);
        //lineChart.setTitle("Pressione");

    }


    public static void main(String[] args) {
        launch(args);

    }


}
