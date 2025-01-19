/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import connect4.Connect4;
import connect4.Player;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class AutenticarseController implements Initializable {

    @FXML
    private Button autenticar;
    @FXML
    private TextField nicknameField;
    @FXML
    private TextField passwordField;

    Image icono = new Image(getClass().getResourceAsStream("/resources/images/4raya.png"));

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    @FXML
    private void autenticarButton(ActionEvent event) {
        String nickname = nicknameField.getText();
        String password = passwordField.getText();

        
        Connect4 connect4 = Connect4.getInstance();
        Player player = connect4.loginPlayer(nickname, password);

        if (player != null) {
            
            openGameWindow();
        } else {
            
            showAlert("Error", "No se pudo encontrar a un usuario con dicha contraseña.");
        }
    }
    private void openGameWindow() {
        try {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            MenuController gameController = loader.getController();
            
            gameController.setPlayerNickname(nicknameField.getText());
            stage.getIcons().add(icono);
            stage.show();
            
            Stage currentStage = (Stage) autenticar.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void recuperarPassword(ActionEvent event) {
        try {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RecuperarPassword.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            
            stage.show();
            stage.getIcons().add(icono);
            
            Stage currentStage = (Stage) autenticar.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void regresar(ActionEvent event) {
        try {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.getIcons().add(icono);
            stage.show();
            
            Stage currentStage = (Stage) autenticar.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
