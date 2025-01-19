/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import connect4.Connect4;
import connect4.Round;
import java.net.URL;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.stage.Stage;


/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class VerGanadasController implements Initializable {

    @FXML
    private TableView<Round> tablaRound;
    @FXML
    private TableColumn<Round, String> ganadorRound;
    @FXML
    private TableColumn<Round, String> perdedorRound;
    @FXML
    private TableColumn<Round, String> fechaRound;
    @FXML
    private Button nottonRegresar;
    
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
        Stage currentStage = (Stage) tablaRound.getScene().getWindow();
        currentStage.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    }
    
    public void cargarDatos(List<Round> partidas, LocalDateTime inicio, LocalDateTime fin) {
        //Connect4 connect4 = Connect4.getInstance();

    
    //List<Round> todasPartidas = obtenerTodasLasPartidas();
    List<Round> partidasFiltradas = partidas.stream()
            .filter(round -> !round.getTimestamp().isBefore(inicio) && !round.getTimestamp().isAfter(fin))
            .sorted((r1, r2) -> r2.getTimestamp().compareTo(r1.getTimestamp())) // Ordenar por fecha descendente
            .collect(Collectors.toList());

    // Validar si no hay partidas filtradas
    if (partidasFiltradas.isEmpty()) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Sin partidas");
        alerta.setHeaderText(null);
        alerta.setContentText("No se encontraron partidas en el rango de fechas seleccionado.");
        alerta.showAndWait();
        return;
    }

    // Configurar columnas
    fechaRound.setCellValueFactory(cellData -> {
    LocalDateTime timestamp = cellData.getValue().getTimestamp();
    if (timestamp != null) {
        return new SimpleStringProperty(timestamp.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
    } else {
        return new SimpleStringProperty(""); // O cualquier valor predeterminado
    }
});
    ganadorRound.setCellValueFactory(round -> 
        new SimpleStringProperty(round.getValue().getWinner().getNickName())
    );
    perdedorRound.setCellValueFactory(round -> 
        new SimpleStringProperty(round.getValue().getLoser().getNickName())
    );

    // Asignar datos a la tabla
    tablaRound.setItems(FXCollections.observableArrayList(partidasFiltradas));
}

  private List<Round> obtenerTodasLasPartidas() {
    Connect4 connect4 = Connect4.getInstance();

    return connect4.getRanking().stream()
            .flatMap(player -> connect4.getRoundsForPlayer(player.getNickName()).stream())
            .distinct()
            .collect(Collectors.toList());
}  
  public void setPlayerNickname(String playerNickname) {
        nickName = playerNickname; 
    }
}
