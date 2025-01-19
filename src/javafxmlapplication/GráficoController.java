/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import java.net.URL;
import java.time.LocalDate;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class GráficoController implements Initializable {

    @FXML
    private LineChart<String, Number> grafico;
    
    private String nickName;
    
    Image icono = new Image(getClass().getResourceAsStream("/resources/images/4raya.png"));


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    } 
    
    public void cargarDatos(Map<LocalDate, Integer> datos) {
        grafico.getData().clear();

        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Partidas por Día");

        for (Map.Entry<LocalDate, Integer> entrada : datos.entrySet()) {
            serie.getData().add(new XYChart.Data<>(entrada.getKey().toString(), entrada.getValue()));
        }

        grafico.getData().add(serie);
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
        Stage currentStage = (Stage) grafico.getScene().getWindow();
        currentStage.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    }
    
    public void setPlayerNickname(String playerNickname) {
        nickName = playerNickname;
    }
    
}
