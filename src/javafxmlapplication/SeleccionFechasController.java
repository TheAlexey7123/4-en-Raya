/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.stage.Stage;


/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class SeleccionFechasController implements Initializable {

    @FXML
    private DatePicker fechaInicio;
    @FXML
    private DatePicker fechaFin;
    
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
    private void regresar(ActionEvent event) {
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
            Stage currentStage = (Stage) fechaInicio.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void verPartidas(ActionEvent event) {
        LocalDate inicio = fechaInicio.getValue();
        LocalDate fin = fechaFin.getValue();

        if (inicio == null || fin == null) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Fechas inválidas");
            alerta.setHeaderText(null);
            alerta.setContentText("Por favor, selecciona ambas fechas.");
            alerta.showAndWait();
            return;
        }

        
        if (inicio.isAfter(fin)) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Rango inválido");
            alerta.setHeaderText(null);
            alerta.setContentText("La fecha de inicio no puede ser posterior a la fecha de fin.");
            alerta.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ListadoPartidas.fxml"));
            Parent root = loader.load();

            // Pasar datos al controlador de la nueva ventana
            ListadoPartidasController controlador = loader.getController();
            controlador.cargarPartidas(inicio.atStartOfDay(), fin.atTime(23, 59, 59));
            controlador.setPlayerNickname(nickName);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Partidas realizadas");
            stage.getIcons().add(icono);

            stage.show();

            // Cerrar la ventana actual
            Stage currentStage = (Stage) fechaInicio.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    public void setPlayerNickname(String playerNickname) {
        nickName = playerNickname;
    }
}
