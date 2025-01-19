/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import connect4.Connect4;
import connect4.Player;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class InvitadoAutenticarseController implements Initializable {

    @FXML
    private TextField usuarioField;
    @FXML
    private TextField passwordField;
    
    private String nickName;
    
    Image icono = new Image(getClass().getResourceAsStream("/resources/images/4raya.png"));

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void autenticarseButton(ActionEvent event) {
        if(usuarioField.getText().equals(nickName)){
            showAlert("Error", "No puedes jugar contra ti mismo");
            return;
        }
        String nickname = usuarioField.getText();
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TableroJugador.fxml"));
            Scene gameScene = new Scene(loader.load());
            gameScene.getStylesheets().add(getClass().getResource("/estilos/modoOscuro.css").toExternalForm());


            TableroJugadorController gameController = loader.getController();
            gameController.setPlayerNickname(nickName, usuarioField.getText());
            
            Stage stage = new Stage();
            stage.setScene(gameScene);
            stage.getIcons().add(icono);
            stage.show();

            Stage currentStage = (Stage) usuarioField.getScene().getWindow();
            currentStage.close();
            
        } catch (Exception e) {
        e.printStackTrace();
        }
    }

    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void regresar(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
        Parent root = loader.load();
        
        MenuController gameController = loader.getController();
        gameController.setPlayerNickname(nickName);
                
        Stage stage = (Stage) usuarioField.getScene().getWindow();
        stage.getIcons().add(icono);

        stage.getScene().setRoot(root);
        stage.show();
    }
    
    public void setPlayerNickname1(String playerNickname) {
        nickName = playerNickname;
    }
    
}
