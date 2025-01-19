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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class SeleccionarGraficoController implements Initializable {

    @FXML
    private DatePicker fechaInicio;
    @FXML
    private DatePicker fechaFin;
    @FXML
    private Button buscarButton;
    
    private String  nickName;
    
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
         // Validar las fechas
        LocalDate inicio = fechaInicio.getValue();
        LocalDate fin = fechaFin.getValue();

        if (inicio == null || fin == null) {
            mostrarError("Por favor, selecciona ambas fechas.");
            return;
        }

        if (inicio.isAfter(fin)) {
            mostrarError("La fecha de inicio no puede ser posterior a la fecha de fin.");
            return;
        }

        // Obtener las partidas en el rango
        Map<LocalDate, Integer> partidasPorDia = obtenerPartidasPorDia(inicio, fin);

        // Mostrar el gráfico
        mostrarGrafico(partidasPorDia);
    }

    private void mostrarError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private Map<LocalDate, Integer> obtenerPartidasPorDia(LocalDate inicio, LocalDate fin) {
        Connect4 sistema = Connect4.getInstance();

        // Crear un conjunto para almacenar todas las rondas (evitar duplicados)
        Set<Round> todasLasPartidas = new HashSet<>();

        // Iterar sobre todos los jugadores registrados
        List<Player> jugadores = sistema.getRanking(); // Obtiene todos los jugadores
        for (Player jugador : jugadores) {
            // Agregar todas las rondas del jugador al conjunto
            todasLasPartidas.addAll(sistema.getRoundsForPlayer(jugador.getNickName()));
        }

        // Filtrar y contar las partidas por día
        Map<LocalDate, Integer> partidasPorDia = new TreeMap<>();
        for (Round partida : todasLasPartidas) {
            LocalDate fechaPartida = partida.getTimestamp().toLocalDate();
            if (!fechaPartida.isBefore(inicio) && !fechaPartida.isAfter(fin)) {
                partidasPorDia.put(fechaPartida, partidasPorDia.getOrDefault(fechaPartida, 0) + 1);
            }
        }
        return partidasPorDia;
    }

    private void mostrarGrafico(Map<LocalDate, Integer> datos) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Gráfico.fxml"));
            Parent root = loader.load();

            GráficoController controller = loader.getController();
            controller.setPlayerNickname(nickName);
            controller.cargarDatos(datos);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Gráfico de Partidas");
            stage.getIcons().add(icono);

            stage.show();
            
            Stage currentStage = (Stage) fechaFin.getScene().getWindow();
            currentStage.close();
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

        // Obtener el controlador de la nueva escena
        DatosTablasController rankingController = loader.getController();
        rankingController.initializeWithNickname(this.nickName);

        

        // Crear y mostrar la nueva ventana
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
