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
public class SeleccionarJugadorController implements Initializable {

    @FXML
    private DatePicker fechaInicio1;
    @FXML
    private DatePicker fechaFin1;
    @FXML
    private TextField usuarioText;
    @FXML
    private Button bpttpnBuscar;
    
    private String nickName1;
    
    Image icono = new Image(getClass().getResourceAsStream("/resources/images/4raya.png"));


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
       // bpttpnBuscar.setOnMouseClicked(this::buscar);
    }    

    @FXML
    private void buscar(ActionEvent event) {
        String nickName = usuarioText.getText();
        LocalDate fechaInicio = fechaInicio1.getValue();
        LocalDate fechaFin = fechaFin1.getValue();

        // Validaciones básicas
        if (nickName == null || nickName.isEmpty()) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Usuario inválido");
            alerta.setHeaderText(null);
            alerta.setContentText("Por favor, selecciona un usuario.");
            alerta.showAndWait();
            return;
        }

        if (fechaInicio == null || fechaFin == null) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Fechas inválidas");
            alerta.setHeaderText(null);
            alerta.setContentText("Por favor, selecciona ambas fechas.");
            alerta.showAndWait();
            return;
        }

        if (fechaInicio.isAfter(fechaFin)) {
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

        List<Round> partidas = sistema.getRoundsForPlayer(nickName)
                .stream()
                .filter(round -> !round.getTimestamp().toLocalDate().isBefore(fechaInicio) &&
                                !round.getTimestamp().toLocalDate().isAfter(fechaFin))
                .sorted((r1, r2) -> r2.getTimestamp().compareTo(r1.getTimestamp()))
                .collect(Collectors.toList());

        // Aquí pasas la lista de partidas a la siguiente escena (FXML para mostrar las partidas)
        mostrarPartidas(partidas, fechaInicio, fechaFin);
    }

    private void mostrarError(String mensaje) {
        // Mostrar un mensaje de error al usuario
        System.err.println(mensaje); 
    }

    private void mostrarPartidas(List<Round> partidas, LocalDate fechaInicio, LocalDate fechaFin) {
        // Código para cargar la siguiente escena (FXML) y pasarle las partidas
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("MostrarPartidas.fxml"));
            javafx.scene.Parent root = loader.load();

            MostrarPartidasController controller = loader.getController();
            controller.cargarPartidas(partidas, fechaInicio.atStartOfDay(), fechaFin.atTime(23, 59, 59));
            controller.setPlayerNickname(nickName1);

            Stage stage = (Stage) bpttpnBuscar.getScene().getWindow();
            stage.getIcons().add(icono);

            stage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setPlayerNickname(String playerNickname) {
        nickName1 = playerNickname; 
    }

    @FXML
    private void regresar(ActionEvent event) {
        try {
        // Cargar el archivo FXML del ranking
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DatosTablas.fxml"));
        Scene rankingScene = new Scene(loader.load());

        // Obtener el controlador de la nueva escena
        DatosTablasController rankingController = loader.getController();
        rankingController.initializeWithNickname(this.nickName1);

        // Inicializar datos si es necesario
        //rankingController.initializeWithSomeData(); // Cambia esto según tus necesidades

        // Crear y mostrar la nueva ventana
        Stage stage = new Stage();
        stage.setScene(rankingScene);
        stage.getIcons().add(icono);

        stage.show();

        // Cerrar la ventana actual
        Stage currentStage = (Stage) fechaInicio1.getScene().getWindow();
        currentStage.close();
    } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    }
    

