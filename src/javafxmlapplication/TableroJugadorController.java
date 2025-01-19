/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import connect4.Connect4;
import connect4.Player;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.io.IOException;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static javafxmlapplication.Utils.*;

/**
 * FXML Controller class
 *
 * @author alexey
 */
public class TableroJugadorController implements Initializable {

    private static final int TAMANO_CELDA = 80;
    private static final int COLUMNAS = 7;
    private static final int FILAS = 6;
    int movimientos = 0;

    private boolean esRoja = true;
    private boolean[][] tablero = new boolean[COLUMNAS][FILAS];
    private int[][] tableroVictoria = new int[COLUMNAS][FILAS];
    
    private int victoriasJugador1 = 0;
    private int victoriasJugador2 = 0;
    private final int victoriasNecesarias = 3;
    
    Image icono = new Image(getClass().getResourceAsStream("/resources/images/4raya.png"));
    
    @FXML
    private ImageView btnOscuro;
    
    @FXML
    private GridPane gridPane;

    private Pane raizCirculos;
    @FXML
    private VBox col0, col1, col2, col3,col4,col5;
    
    @FXML
    private VBox col10, col11,col12,col13,col14,col15;

    @FXML
    private VBox col20,col21,col22,col23,col24,col25;

    @FXML
    private VBox col30, col31,col32, col33, col34, col35;

    @FXML
    private VBox col40, col41,col42,col43,col44,col45;

    @FXML
    private VBox col50, col51,col52,col53,col54,col55;
    
    @FXML
    private VBox col60, col61,col62,col63,col64,col65;
    @FXML
    private Label turnoDe;
    
    private String nickName1;
    
    private String nickName2;
    
    private boolean esModoOscuro = true;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        raizCirculos = new Pane();
        gridPane.getChildren().add(raizCirculos);
        gridPane.getChildren().addAll();
                
        VBox[] grupo1 = {col0, col1, col2, col3, col4, col5};
        VBox[] grupo2 = {col10, col11, col12, col13, col14, col15};
        VBox[] grupo3 = {col20, col21, col22, col23, col24, col25};
        VBox[] grupo4 = {col30, col31, col32, col33, col34, col35};
        VBox[] grupo5 = {col40, col41, col42, col43, col44, col45};
        VBox[] grupo6 = {col50, col51, col52, col53, col54, col55};
        VBox[] grupo7 = {col60, col61, col62, col63, col64, col65};

        agregarHoverAGrupo(grupo1);
        agregarHoverAGrupo(grupo2);
        agregarHoverAGrupo(grupo3);
        agregarHoverAGrupo(grupo4);
        agregarHoverAGrupo(grupo5);
        agregarHoverAGrupo(grupo6);
        agregarHoverAGrupo(grupo7);
        
        initTablero();
        
        String nombre1 = getNickName1();
        String nombre2 = getNickName2();
        if (nombre1 == null || nombre1.isEmpty()) {
            nombre1 = "Jugador 1";
        }
        if (nombre2 == null || nombre2.isEmpty()) {
            nombre2 = "Jugador 2";
        }

        turnoDe.setText("Turno de " + nombre1);
    }

    //rellenar nuestras variables a false y -1
    public void initTablero(){
        for (int i = 0; i < COLUMNAS; i++) {
            for (int j = 0; j < FILAS; j++) {
                tablero[i][j] = false;
                tableroVictoria[i][j] = -1;
            }
        }
    }

    public void setPlayerNickname(String nickName1, String nickName2) {
        this.nickName1 = nickName1;
        this.nickName2 = nickName2;
        actualizarNombre();
    }
    
    
    public String getNickName1(){
        return nickName1;
    }
    
    public String getNickName2(){
        return nickName2;
    }
    
    public void actualizarNombre(){
        if(esRoja){
            turnoDe.setText("Turno de " + nickName1);
        }
        
        else{
            turnoDe.setText("Turno de " + nickName2);
        }
    }
    
    private void agregarHoverAGrupo(VBox[] grupo) {
        for (VBox columna : grupo) {
            columna.getStyleClass().add("columna"); // Añadir CSS para cada columna

            columna.setOnMouseEntered(e -> {
                for (VBox c : grupo) {
                    c.getStyleClass().add("columna-hover");
                }
            });

            columna.setOnMouseExited(e -> {
                for (VBox c : grupo) {
                    c.getStyleClass().remove("columna-hover");
                }
            });
        }
    }

    private int[] filasOcupadas = new int[COLUMNAS];
    private boolean juegoTerminado = false;
   
    //Cuando hacemos clic
    @FXML
    private void caerBola(MouseEvent event) throws IOException {
        if(juegoTerminado){
            return;
        }
        
        movimientos++;
        System.out.println("Click detectado en X: " + event.getSceneX());

        int columna = columnCalc(gridPane, event.getSceneX());
        System.out.println("Columna calculada: " + columna);

        if (columna < 0 || columna >= COLUMNAS) {
            System.out.println("Columna fuera de rango: " + columna);
            return;
        }

        // Verificar si la columna ya está llena
        if (filasOcupadas[columna] >= FILAS) {
            System.out.println("Columna llena, no se puede agregar más fichas.");
            return;
        }

        int fila = FILAS - 1 - filasOcupadas[columna]; 

        Circulo circulo = new Circulo(esRoja); 

        double radio = Math.min(gridPane.getWidth() / COLUMNAS, gridPane.getHeight() / FILAS) / 2.0;
        circulo.setRadius(radio); 
        
        GridPane.setHalignment(circulo, javafx.geometry.HPos.CENTER);
        GridPane.setValignment(circulo, javafx.geometry.VPos.CENTER);
        
        Pane contenedor = new Pane(circulo);
        
        GridPane.setHgrow(contenedor, javafx.scene.layout.Priority.ALWAYS);
        
        circulo.radiusProperty().bind(Bindings.createDoubleBinding(
            () -> Math.min(contenedor.getWidth(), contenedor.getHeight()) / 2 - 5,
            contenedor.widthProperty(),
            contenedor.heightProperty()
        ));

        gridPane.add(contenedor, columna, fila);
        gridPane.add(circulo, columna, fila); 

        circulo.setFill(esRoja ? Color.RED : Color.YELLOW);

        tablero[columna][fila] = esRoja;
        tableroVictoria[columna][fila] = esRoja ? 1: 0;
        filasOcupadas[columna]++;

        comprobarVictoria(fila, columna);
        
        esRoja = !esRoja;
        actualizarNombre();
    }
    
    private void hayEmpate() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Empate");
        alert.setHeaderText("Empate");
        alert.setContentText("El tablero está lleno y no hay ganador. ¡Es un empate!");
        alert.showAndWait();
    }

    
    private void comprobarVictoria(int fila, int columna) throws IOException{
        if (verificarVictoria(columna, fila)) {
            String ganador = esRoja ? getNickName1() : getNickName2();
            String perdedor = !esRoja ? getNickName1() : getNickName2();
            if (esRoja) {
                victoriasJugador1++;
            } else {
                victoriasJugador2++;
            }

            mostrarAlertaVictoria(ganador);

            if (victoriasJugador1 >= victoriasNecesarias || victoriasJugador2 >= victoriasNecesarias) {
                mostrarGanadorDefinitivo(ganador, perdedor);
                reiniciarContadores();
                salir();
            } else {
                reiniciarJuego();
            }
        }
        
        else if(tableroLleno()){
            hayEmpate();
            reiniciarJuego();
        }
    }
    
    private boolean tableroLleno() {
        for (int i = 0; i < COLUMNAS; i++) {
            if (filasOcupadas[i] < FILAS) {
                return false;
            }
        }
        return true;
    }
    
    public boolean esGanador1(){
        return victoriasJugador1 >= victoriasNecesarias;
    }
    
    private void reiniciarContadores() {
        victoriasJugador1 = 0;
        victoriasJugador2 = 0;
    }
    
    private void mostrarAlertaVictoria(String ganador) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("¡Victoria!");
        alert.setHeaderText(null);
        alert.setContentText("¡" + ganador + " ha ganado esta partida!\n" +
                             "Victorias " + getNickName1() + ": " + victoriasJugador1 + "\n" +
                             "Victorias " + getNickName2() + ": " + victoriasJugador2);       
        alert.showAndWait();
    }
    
    private void mostrarGanadorDefinitivo(String ganador, String perdedor) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Ganador de la partida");
        alert.setHeaderText(null);
        alert.setContentText("¡" + ganador + " ha ganado contra " + perdedor + "!");
        
        Connect4 connect4 = Connect4.getInstance();
        Player p = connect4.getPlayer(nickName1);
        Player f = connect4.getPlayer(nickName2);

        connect4.registerRound(LocalDateTime.now(), p, f);
        p.addPoints(3);
        f.addPoints(-3);
        
        alert.showAndWait();
    }

    
    private void reiniciarJuego() throws IOException {
        
        boolean oscuroAux = esModoOscuro;
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TableroJugador.fxml"));
        Parent root = loader.load();

        TableroJugadorController nuevoController = loader.getController();
        nuevoController.setPlayerNickname(nickName1, nickName2);
        nuevoController.setContadores(victoriasJugador1, victoriasJugador2);

        Stage stage = (Stage) turnoDe.getScene().getWindow();
        stage.getIcons().add(icono);
        
        if (!oscuroAux) {
            stage.getScene().getStylesheets().clear();
            stage.getScene().getStylesheets().add(getClass().getResource("/estilos/modoClaro.css").toExternalForm());
            btnOscuro.setImage(new Image(getClass().getResourceAsStream("/resources/images/modoClaro.png")));
        }

        stage.getScene().setRoot(root);
    }
    
    public void setContadores(int victoriasJugador1, int victoriasJugador2) {
        this.victoriasJugador1 = victoriasJugador1;
        this.victoriasJugador2 = victoriasJugador2;
    }

    private boolean verificarVictoria(int columna, int fila) {
        int color = tableroVictoria[columna][fila];
        return color != -1 && (verificarDireccion(columna, fila, 1, 0, color) || 
                               verificarDireccion(columna, fila, 0, 1, color) || 
                               verificarDireccion(columna, fila, 1, 1, color) || 
                               verificarDireccion(columna, fila, 1, -1, color));
    }
    
    private void salir() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
        Parent root = loader.load();
        
        MenuController gameController = loader.getController();
        gameController.setPlayerNickname(nickName1);
        
        Stage stage = (Stage) turnoDe.getScene().getWindow();
        stage.getScene().setRoot(root);
        stage.show();
    }

    private boolean verificarDireccion(int col, int fila, int deltaCol, int deltaFila, int color) {
        int cuenta = 1;
        cuenta += contarFichas(col, fila, deltaCol, deltaFila, color);
        cuenta += contarFichas(col, fila, -deltaCol, -deltaFila, color);
        return cuenta >= 4;
    }

    private int contarFichas(int col, int fila, int deltaCol, int deltaFila, int color) {
        int cuenta = 0;
        int c = col + deltaCol;
        int f = fila + deltaFila;
        while (c >= 0 && c < COLUMNAS && f >= 0 && f < FILAS && tableroVictoria[c][f] == color) {
            cuenta++;
            c += deltaCol;
            f += deltaFila;
        }
        return cuenta;
    }
    
    @FXML
    private void cambiarModoOscuro(MouseEvent event) {
        Scene scene = ((Node) event.getSource()).getScene();
        
        if (esModoOscuro) {
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("/estilos/modoClaro.css").toExternalForm());
            btnOscuro.setImage(new Image(getClass().getResourceAsStream("/resources/images/modoClaro.png")));
        } else {
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("/estilos/modoOscuro.css").toExternalForm());
            btnOscuro.setImage(new Image(getClass().getResourceAsStream("/resources/images/modoOscuro.png")));
        }

        esModoOscuro = !esModoOscuro;
    }
}
