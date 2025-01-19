/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import connect4.Connect4;
import connect4.Player;
import connect4.Round;
import java.io.IOException;
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
public class SeleccionarGrafico2Controller implements Initializable {

    @FXML
    private TextField usuarioText;
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
        String nombreUsuario = usuarioText.getText();
        LocalDate inicio = fechaInicio.getValue();
        LocalDate fin = fechaFin.getValue();

        if (nombreUsuario.isEmpty() || inicio == null || fin == null) {
            mostrarError("Por favor, rellena todos los campos.");
            return;
        }

        if (inicio.isAfter(fin)) {
            mostrarError("La fecha de inicio no puede ser posterior a la fecha de fin.");
            return;
        }

        // Obtener los datos
        Map<LocalDate, Integer[]> partidasDatos = obtenerPartidasGanadasYPerdidas(nombreUsuario, inicio, fin);
        Map<LocalDate, Integer> oponentesPorDia = obtenerOponentesPorDia(nombreUsuario, inicio, fin);

        // Mostrar los gráficos
        mostrarGrafico(partidasDatos, oponentesPorDia);
    }

    private void mostrarError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private Map<LocalDate, Integer[]> obtenerPartidasGanadasYPerdidas(String nombreUsuario, LocalDate inicio, LocalDate fin) {
        Connect4 sistema = Connect4.getInstance();
        Player jugador = sistema.getPlayer(nombreUsuario);
        if (jugador == null) {
            mostrarError("El jugador no existe.");
            return null;
        }

        // Obtener las partidas de este jugador en el periodo especificado
        List<Round> rondas = sistema.getRoundsForPlayer(nombreUsuario);

        // Filtrar las rondas en el rango de fechas
        Map<LocalDate, Integer[]> partidasPorDia = new TreeMap<>();
        for (Round ronda : rondas) {
            LocalDate fechaPartida = ronda.getTimestamp().toLocalDate();
            if (!fechaPartida.isBefore(inicio) && !fechaPartida.isAfter(fin)) {
                Integer[] resultados = partidasPorDia.getOrDefault(fechaPartida, new Integer[]{0, 0}); // [perdidas, ganadas]
                if (ronda.getWinner().equals(jugador)) {
                    resultados[1]++; 
                } else {
                    resultados[0]++; 
                }
                partidasPorDia.put(fechaPartida, resultados);
            }
        }
        return partidasPorDia;
    }

    private Map<LocalDate, Integer> obtenerOponentesPorDia(String nombreUsuario, LocalDate inicio, LocalDate fin) {
        Connect4 sistema = Connect4.getInstance();
        Player jugador = sistema.getPlayer(nombreUsuario);
        if (jugador == null) {
            mostrarError("El jugador no existe.");
            return null;
        }

        List<Round> rondas = sistema.getRoundsForPlayer(nombreUsuario);
        Map<LocalDate, Set<String>> oponentesPorDia = new TreeMap<>();
        
        for (Round ronda : rondas) {
            LocalDate fechaPartida = ronda.getTimestamp().toLocalDate();
            if (!fechaPartida.isBefore(inicio) && !fechaPartida.isAfter(fin)) {
                Set<String> oponentes = oponentesPorDia.getOrDefault(fechaPartida, new HashSet<>());
                oponentes.add(ronda.getWinner().equals(jugador) ? ronda.getLoser().getNickName() : ronda.getWinner().getNickName());
                oponentesPorDia.put(fechaPartida, oponentes);
            }
        }

        Map<LocalDate, Integer> resultado = new TreeMap<>();
        for (Map.Entry<LocalDate, Set<String>> entry : oponentesPorDia.entrySet()) {
            resultado.put(entry.getKey(), entry.getValue().size());
        }
        return resultado;
    }

    private void mostrarGrafico(Map<LocalDate, Integer[]> partidasDatos, Map<LocalDate, Integer> oponentesPorDia) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Grafico2.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            Grafico2Controller controller = loader.getController();
            controller.cargarDatos(partidasDatos, oponentesPorDia);
            controller.setPlayerNickname(nickName);

            stage.setTitle("Gráficos de Estadísticas");
            stage.getIcons().add(icono);
            stage.show();
            Stage currentStage = (Stage) fechaInicio.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void setPlayerNickname(String playerNickname) {
        nickName = playerNickname; 
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
    
}
