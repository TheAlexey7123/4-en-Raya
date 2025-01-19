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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class Grafico2Controller implements Initializable {

    @FXML
    private BarChart<String, Number> graficoPartidas;
    @FXML
    private BarChart<String, Number> graficoOponentes;
    @FXML
    private NumberAxis yAxisPartidas;
    @FXML
    private CategoryAxis xAxisPartidas;
    @FXML
    private NumberAxis yAxisOponentes;
    @FXML
    private CategoryAxis xAxisOponentes;
    
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

        Stage currentStage = (Stage) yAxisPartidas.getScene().getWindow();
        currentStage.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    }
    
    public void cargarDatos(Map<LocalDate, Integer[]> partidasDatos, Map<LocalDate, Integer> oponentesPorDia) {
        graficoPartidas.getData().clear();
        graficoOponentes.getData().clear();

        XYChart.Series<String, Number> serieGanadas = new XYChart.Series<>();
        serieGanadas.setName("Ganadas");
        XYChart.Series<String, Number> seriePerdidas = new XYChart.Series<>();
        seriePerdidas.setName("Perdidas");

        for (Map.Entry<LocalDate, Integer[]> entry : partidasDatos.entrySet()) {
            String fecha = entry.getKey().toString();
            Integer[] resultados = entry.getValue();
            serieGanadas.getData().add(new XYChart.Data<>(fecha, resultados[1]));
            seriePerdidas.getData().add(new XYChart.Data<>(fecha, resultados[0]));
        }

        graficoPartidas.getData().addAll(seriePerdidas, serieGanadas);

        XYChart.Series<String, Number> serieOponentes = new XYChart.Series<>();
        serieOponentes.setName("Oponentes");

        for (Map.Entry<LocalDate, Integer> entry : oponentesPorDia.entrySet()) {
            serieOponentes.getData().add(new XYChart.Data<>(entry.getKey().toString(), entry.getValue()));
        }

        graficoOponentes.getData().add(serieOponentes);
    }
    
    public void setPlayerNickname(String playerNickname) {
        nickName = playerNickname;
    }
    
}
