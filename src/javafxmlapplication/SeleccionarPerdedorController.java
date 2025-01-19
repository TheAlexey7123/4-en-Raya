/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import connect4.Connect4;
import connect4.Player;
import connect4.Round;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class SeleccionarPerdedorController implements Initializable {

    @FXML
    private TextField usuarioText;
    @FXML
    private DatePicker fechaInicio;
    @FXML
    private DatePicker fechaFin;
    @FXML
    private Button buscarButton;
    @FXML
    private Button regresarButton;
    
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
   private void buscar(ActionEvent event) {
        String nickName = usuarioText.getText();
        LocalDate FechaInicio = fechaInicio.getValue();
        LocalDate FechaFin = fechaFin.getValue();

        // Validaciones básicas
        if (nickName == null || nickName.isEmpty()) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Usuario inválido");
            alerta.setHeaderText(null);
            alerta.setContentText("Por favor, selecciona un usuario.");
            alerta.showAndWait();
            return;
        }

        if (FechaInicio == null || FechaFin == null) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Fechas inválidas");
            alerta.setHeaderText(null);
            alerta.setContentText("Por favor, selecciona ambas fechas.");
            alerta.showAndWait();
            return;
        }

        if (FechaInicio.isAfter(FechaFin)) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Rango inválido");
            alerta.setHeaderText(null);
            alerta.setContentText("La fecha de inicio no puede ser posterior a la fecha de fin.");
            alerta.showAndWait();
            return;
        }

        // Conexión al sistema para obtener las partidas
        Connect4 sistema = Connect4.getInstance();
        Player jugador = sistema.getPlayer(nickName);

        if (jugador == null) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Usuario inválido");
            alerta.setHeaderText(null);
            alerta.setContentText("El usuario no existe en el sistema.");
            alerta.showAndWait();
            return;
        }
        

        // Obtener las partidas perdidass dentro del rango de fechas
        List<Round> partidasPerdidas = sistema.getRoundsForPlayer(nickName)
                .stream()
                .filter(round -> round.getLoser().getNickName().equals(nickName)) // Solo partidas perdidas
                .filter(round -> !round.getTimestamp().toLocalDate().isBefore(FechaInicio) &&
                                 !round.getTimestamp().toLocalDate().isAfter(FechaFin)) // Filtrar por fechas
                .sorted((r1, r2) -> r2.getTimestamp().compareTo(r1.getTimestamp())) // Ordenar por fecha descendente
                .collect(Collectors.toList());

        // Mostrar las partidas en la siguiente escena
        mostrarPartidas(partidasPerdidas, FechaInicio, FechaFin);
    }

    private void mostrarError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void mostrarPartidas(List<Round> partidas, LocalDate fechaInicio, LocalDate fechaFin) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("VerPerdidas.fxml"));
            javafx.scene.Parent root = loader.load();

            VerPerdidasController controller = loader.getController();
            controller.cargarDatos(partidas, fechaInicio.atStartOfDay(), fechaFin.atTime(23, 59, 59));
            controller.setPlayerNickname(nickName);

            Stage stage = (Stage) buscarButton.getScene().getWindow();
            stage.getIcons().add(icono);

            stage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        
    

    @FXML
    private void regresar(ActionEvent event) {
        try {
        // Cargar el archivo FXML del ranking
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

    public void setPlayerNickname(String playerNickname) {
        nickName = playerNickname;
    }
}


