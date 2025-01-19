/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import connect4.Connect4;
import connect4.Player;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class DatosTablasController implements Initializable {

    @FXML
    private TableView<connect4.Player> tablaPersonas;
    @FXML
    private TableColumn<connect4.Player, String> nombreJugador;
    @FXML
    private TableColumn<connect4.Player, Integer> puntosJugador;
    @FXML
    private TableColumn<connect4.Player, ImageView> avatarJugador;
    @FXML
    private TextField campoBusqueda;
    private String nickName;
    @FXML
    private Text usuario;
    @FXML
    private Button fechasBotton;
    @FXML
    private Button jugadorBotton;
    @FXML
    private Button ganadasBotton;
    @FXML
    private Button perdidasBotton;
    @FXML
    private Button realizadasBotton;
    @FXML
    private Button promedioBotton;
    
    Image icono = new Image(getClass().getResourceAsStream("/resources/images/4raya.png"));

    /**
     * Initializes the controller class.
     * 
     */
    public void initializeWithNickname(String nickname) {
        Connect4 connect4 = Connect4.getInstance();
        Player p = connect4.getPlayer(nickname); 

        nickName = p.getNickName();
        nombreJugador.setCellValueFactory(player -> new SimpleStringProperty(player.getValue().getNickName()));
        puntosJugador.setCellValueFactory(player -> new SimpleObjectProperty<>(player.getValue().getPoints()));
        avatarJugador.setCellValueFactory(player -> {
            ImageView avatarView = new ImageView(player.getValue().getAvatar());
            avatarView.setFitWidth(50);
            avatarView.setFitHeight(50);
            avatarView.setPreserveRatio(true);
            return new SimpleObjectProperty<>(avatarView);
        });

        cargarRanking();
         
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
       
    }
    private void cargarRanking() {

        List<Player> jugadores = Connect4.getInstance().getRanking();
        tablaPersonas.setItems(FXCollections.observableArrayList(jugadores));
    }
    @FXML
    private void buscarJugador() {
        String nombre = campoBusqueda.getText().trim().toLowerCase();
        if (!nombre.isEmpty()) {
            for (Player jugador : tablaPersonas.getItems()) {
                if (jugador.getNickName().toLowerCase().contains(nombre)) {
                    tablaPersonas.getSelectionModel().select(jugador);
                    tablaPersonas.scrollTo(jugador);
                    return;
                }
            }

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Jugador no encontrado");
            alerta.setHeaderText(null);
            alerta.setContentText("No se encontró ningún jugador con el nombre: " + nombre);
            alerta.showAndWait();
        }
    }
    
    @FXML
    private void regresar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
            Scene gameScene = new Scene(loader.load());

            
            MenuController gameController = loader.getController();
            
            gameController.setPlayerNickname(this.nickName);

            
            Stage stage = new Stage();
            stage.setScene(gameScene);
            stage.getIcons().add(icono);
            stage.show();

            
            Stage currentStage = (Stage) campoBusqueda.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void seleccionFechas(ActionEvent event) {
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SeleccionFechas.fxml"));
        Scene rankingScene = new Scene(loader.load());

        SeleccionFechasController rankingController = loader.getController();
        rankingController.setPlayerNickname(nickName);

        Stage stage = new Stage();
        stage.setScene(rankingScene);
        stage.getIcons().add(icono);
        stage.show();

        // Cerrar la ventana actual
        Stage currentStage = (Stage) fechasBotton.getScene().getWindow();
        currentStage.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    @FXML
    private void seleccionarJugador(ActionEvent event) {
        try {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("SeleccionarJugador.fxml"));
        Scene rankingScene = new Scene(loader.load());


        SeleccionarJugadorController rankingController = loader.getController();
        rankingController.setPlayerNickname(nickName);

        Stage stage = new Stage();
        stage.setScene(rankingScene);
        stage.getIcons().add(icono);

        stage.show();


        Stage currentStage = (Stage) jugadorBotton.getScene().getWindow();
        currentStage.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    }

    @FXML
    private void seleccionarGanadas(ActionEvent event) {
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SeleccionarGanadas.fxml"));
        Scene rankingScene = new Scene(loader.load());

        SeleccionarGanadasController rankingController = loader.getController();
        rankingController.setPlayerNickname(nickName);

        Stage stage = new Stage();
        stage.setScene(rankingScene);
        stage.getIcons().add(icono);

        stage.show();

        Stage currentStage = (Stage) ganadasBotton.getScene().getWindow();
        currentStage.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    }

    @FXML
    private void seleccionarPerdidas(ActionEvent event) {
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SeleccionarPerdedor.fxml"));
        Scene rankingScene = new Scene(loader.load());


        SeleccionarPerdedorController rankingController = loader.getController();
        rankingController.setPlayerNickname(nickName);


        Stage stage = new Stage();
        stage.setScene(rankingScene);
        stage.getIcons().add(icono);

        stage.show();

        Stage currentStage = (Stage) perdidasBotton.getScene().getWindow();
        currentStage.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    }

    @FXML
    private void selecionarRealizadas(ActionEvent event) {
        try {
  
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SeleccionarGrafico.fxml"));
        Scene rankingScene = new Scene(loader.load());

        SeleccionarGraficoController rankingController = loader.getController();
        rankingController.setPlayerNickname(nickName);

        Stage stage = new Stage();
        stage.setScene(rankingScene);
        stage.getIcons().add(icono);

        stage.show();

        Stage currentStage = (Stage) realizadasBotton.getScene().getWindow();
        currentStage.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    }

    @FXML
    private void seleccionarPromedio(ActionEvent event) {
        try {
 
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SeleccionarGrafico2.fxml"));
        Scene rankingScene = new Scene(loader.load());

        SeleccionarGrafico2Controller rankingController = loader.getController();
        rankingController.setPlayerNickname(nickName);

        Stage stage = new Stage();
        stage.setScene(rankingScene);
        stage.getIcons().add(icono);

        stage.show();

        Stage currentStage = (Stage) promedioBotton.getScene().getWindow();
        currentStage.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    }
}
