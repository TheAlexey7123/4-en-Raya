/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class MenuController implements Initializable {

    @FXML
    private Button bottonMaquina;
    @FXML
    private Button bottonOtroJugador;
    @FXML
    private Button bottonRanking;
    @FXML
    private Button bottonPerfil;
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
    private void perfil(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Perfil.fxml"));
            Scene gameScene = new Scene(loader.load());

            
            PerfilController perfilController = loader.getController();
            
            perfilController.initializeWithNickname(this.nickName);

            
            Stage stage = new Stage();
            stage.setScene(gameScene);
            stage.getIcons().add(icono);
            stage.show();

            
            Stage currentStage = (Stage) bottonRanking.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setPlayerNickname(String playerNickname) {
        nickName = playerNickname;
    }

   @FXML
    private void ranking(ActionEvent event) {
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DatosTablas.fxml"));
        Scene rankingScene = new Scene(loader.load());

        DatosTablasController rankingController = loader.getController();
        rankingController.initializeWithNickname(this.nickName);

        Stage stage = new Stage();
        stage.setScene(rankingScene);
        stage.getIcons().add(icono);
        stage.show();

        // Cerrar la ventana actual
        Stage currentStage = (Stage) bottonRanking.getScene().getWindow();
        currentStage.close();
        } 
        
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Cerrar Sesión");
        alert.setHeaderText(null);
        alert.setContentText("El jugador " + nickName + " ha sido desconectado.");
        alert.showAndWait();

        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
        Scene inicio = new Scene(loader.load());

        
        Stage stage = new Stage();
        stage.setScene(inicio);
        stage.getIcons().add(icono);

        stage.show();
        

        Stage currentStage = (Stage) bottonRanking.getScene().getWindow();
        currentStage.close();
    } catch (IOException e) {
        e.printStackTrace();
         }
    }

    @FXML
    private void contraMaquina(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TableroMaquina.fxml"));
            Scene gameScene = new Scene(loader.load());
            gameScene.getStylesheets().add(getClass().getResource("/estilos/modoOscuro.css").toExternalForm());
            TableroMaquinaController gameController = loader.getController();
            gameController.setPlayerNickname(this.nickName);

            Stage stage = new Stage();
            stage.setScene(gameScene);
            stage.show();
            
            stage.getIcons().add(icono);
            
            Stage currentStage = (Stage) bottonMaquina.getScene().getWindow();
            currentStage.close();
            
        } catch (IOException e) {
            e.printStackTrace();
         }
    }

    

    @FXML
    private void contraJugador(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("InvitadoAutenticarse.fxml"));
            Scene gameScene = new Scene(loader.load());
            InvitadoAutenticarseController gameController = loader.getController();
            gameController.setPlayerNickname1(nickName);

            Stage stage = new Stage();
            
            stage.getIcons().add(icono);
            stage.setScene(gameScene);
            stage.show();

            Stage currentStage = (Stage) bottonOtroJugador.getScene().getWindow();
            currentStage.close();
        } 
        
        catch (IOException e) {
        e.printStackTrace();
        }
    }
    
}
