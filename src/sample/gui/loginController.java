package sample.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import sample.*;

public class loginController {

    @FXML
    private TextField user;

    @FXML
    private PasswordField password;

    @FXML
    private Button loginButton;

    @FXML
    void handleLogin(ActionEvent event) throws Exception{
        if (Datastore.getUsers().isEmpty())
            createDemoUsers();

        for (sample.User current: Datastore.getUsers()) {
            if (current.isValid(user.getText(), password.getText())) {
                //update activeUser
                Datastore.setActiveUser(current);
                break;
            }
            //TODO togli
            else if(user.getText().isEmpty() && password.getText().isEmpty()) {
                User dc = new User("Admin", "Admin", "admin", "admin", UserType.CHIEFDOCTOR);
                Datastore.setActiveUser(dc);
                System.out.println("Using temp chiefdoctor");
                break;
            } else {
                //lancio errore
                showDialog(Alert.AlertType.ERROR, "Utente Errato!");
                return;
            }
        }

        //creo finestra
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("patientList.fxml"));
        Parent root1 = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Lista Pazienti");
        stage.setScene(new Scene(root1));
        stage.show();

        //chiudo login
        stage = (Stage)((Node)event.getTarget()).getScene().getWindow();
        stage.close();
    }

    void showDialog(Alert.AlertType type, String msg){
        Alert alert = new Alert(type);
        alert.setTitle("Login error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    void createDemoUsers(){
        User cd = new User("Demo", "Admin", "admin", "admin", UserType.CHIEFDOCTOR);
        User doc = new User("Demo", "Doctor", "doctor", "doctor", UserType.DOCTOR);
        User nurse = new User("Demo", "Nurse", "nurse", "nurse", UserType.NURSE);

        Datastore.addUser(cd);
        Datastore.addUser(doc);
        Datastore.addUser(nurse);
    }
}

