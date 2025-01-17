package therapy.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import therapy.*;
import java.io.IOException;


public class LoginController {
    @FXML private TextField user;
    @FXML private PasswordField password;


    @FXML
    void handleLogin(ActionEvent event) {
        if (Datastore.getUsers().isEmpty())
            createDemoUsers();

        for (User current: Datastore.getUsers()) {
            if (current.isValid(user.getText(), password.getText())) {
                Datastore.setActiveUser(current);
                break;
            }
        }
        if (Datastore.getActiveUser() == null){
            GUI.showDialog(Alert.AlertType.ERROR, "Login error", "Utente Errato!");
            return;
        }
        // lancio finestra della lista dei pazienti
        Stage stage;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../gui/patientList.fxml"));
            Parent root1 = fxmlLoader.load();
            stage = new Stage();
            stage.setTitle("Lista Pazienti");
            stage.setScene(new Scene(root1));
            stage.setOnCloseRequest(GUI.confirmCloseEventHandler);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/imgs/icon.png")));
            stage.show();
            Datastore.allLoaders.put("patientslist", fxmlLoader);
        }
        catch (IOException e){
            GUI.showDialog(Alert.AlertType.ERROR, "Error!", "Impossibile caricare lista pazienti");
            GUI.quit();
        }

        //chiudo login
        stage = (Stage)((Node)event.getTarget()).getScene().getWindow();
        stage.close();
    }



    private void createDemoUsers(){
        User cd = new User("Demo", "Admin", "admin", "admin", UserType.CHIEFDOCTOR);
        User doc = new User("Demo", "Doctor", "doctor", "doctor", UserType.DOCTOR);
        User nurse = new User("Demo", "Nurse", "nurse", "nurse", UserType.NURSE);

        Datastore.addUser(cd);
        Datastore.addUser(doc);
        Datastore.addUser(nurse);
    }
}

