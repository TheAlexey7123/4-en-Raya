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
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static javafxmlapplication.Utils.*;

/**
 * FXML Controller class
 *
 * @author alexey
 */
public class TableroMaquinaController implements Initializable {

    //private static final int TAMANO_CELDA = 80;
    private static final int COLUMNAS = 7;
    private static final int FILAS = 6;
    int movimientos = 0;

    private boolean esRoja = true;
    private boolean esperandoMaquina = false;
    private boolean dobleClic = false;

    private boolean[][] tablero = new boolean[COLUMNAS][FILAS];
    private int[][] tableroVictoria = new int[COLUMNAS][FILAS];
    
    private int victoriasJugador = 0;
    private int victoriasMaquina = 0;
    private final int victoriasNecesarias = 3;
    
    private boolean ganador = false;
    private boolean esModoOscuro = true;

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
    
    private String nickName = "";
    @FXML
    private ImageView btnOscuro;
    
    Image icono = new Image(getClass().getResourceAsStream("/resources/images/4raya.png"));


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
    
    

    public void setPlayerNickname(String nickname) {
        this.nickName = nickname;
        actualizarNombre();
    }
    
    public String getNickName(){
        return nickName;
    }
    
    private void agregarHoverAGrupo(VBox[] grupo) {
        for (VBox columna : grupo) {
            columna.getStyleClass().add("columna"); // Añadir clase CSS para cada columna

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
    
    //cuando hacemos clic
    @FXML
    private void caerBola(MouseEvent event) throws IOException{
        dobleClic = false;

        caerBolaJugador(event);        
        turnoMaquina();
    }
    
    public void actualizarNombre(){
        String nombre = getNickName();
        if(nombre == null ||nombre.isEmpty()){
            nombre = "Jugador";
        }
        
        if(esRoja){
            turnoDe.setText("Turno de " + nombre);
        }
        
        else{
            turnoDe.setText("Turno de la Máquina");
        }
    }
    
    private void caerBolaJugador(MouseEvent event) throws IOException {
        
        dobleClic = false;

        if (esperandoMaquina) {
            dobleClic = true;
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
        esperandoMaquina = true;
        actualizarNombre();
    }
    
    private void turnoMaquina() {
        if (juegoTerminado) {
            return;
        }
        
        if(dobleClic){
            return;
        }
        
        // Creamos un nuevo hilo porque queremos que la ejecución del jugador sea instantanea y
        // la de la ia que tarde 1.5 segundos para que asi parezca que está pensando
        Thread hiloMaquina = new Thread(() -> {
            try {

                Thread.sleep(1500);

                int columna = (int) (Math.random() * COLUMNAS);

                while (filasOcupadas[columna] >= FILAS) {
                    columna = (int) (Math.random() * COLUMNAS);
                }

                final int columnaFinal = columna;
                Platform.runLater(() -> {
                    try {
                        
                        //lo mismo que en la otra funcion
                        int fila = FILAS - 1 - filasOcupadas[columnaFinal];
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

                        gridPane.add(contenedor, columnaFinal, fila);
                        gridPane.add(circulo, columnaFinal, fila); 

                        circulo.setFill(Color.YELLOW);

                        tablero[columnaFinal][fila] = false;
                        tableroVictoria[columnaFinal][fila] = 0;
                        filasOcupadas[columnaFinal]++;

                        comprobarVictoria(fila, columnaFinal);

                        esRoja = true;
                        esperandoMaquina = false;
                        dobleClic = false;
                        actualizarNombre();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Iniciar el hilo y el daemon es para hacerlo en segundo plano
        hiloMaquina.setDaemon(true);
        hiloMaquina.start();
    }

    private void comprobarVictoria(int fila, int columna) throws IOException{
        if (verificarVictoria(columna, fila)) {
            String ganador = esRoja ? getNickName() : "Máquina";
            String perdedor = !esRoja ? getNickName() : "Máquina";
            if (esRoja) {
                victoriasJugador++;
            } else {
                victoriasMaquina++;
            }

            mostrarAlertaVictoria(ganador);

            if (victoriasJugador >= victoriasNecesarias || victoriasMaquina >= victoriasNecesarias) {
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
    
    private void hayEmpate() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Empate");
        alert.setHeaderText("Empate");
        alert.setContentText("El tablero está lleno y no hay ganador. ¡Es un empate!");
        alert.showAndWait();
    }
    
    private void reiniciarContadores() {
        victoriasJugador = 0;
        victoriasMaquina = 0;
    }
    
    public void setContadores(int victoriasJugador, int victoriasMaquina) {
        this.victoriasJugador = victoriasJugador;
        this.victoriasMaquina = victoriasMaquina;
    }
    
    private void mostrarGanadorDefinitivo(String ganador, String perdedor) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Ganador de la partida");
        alert.setHeaderText(null);
        alert.setContentText("¡" + ganador + " ha ganado contra " + perdedor + "!");
        
        Connect4 connect4 = Connect4.getInstance();
        Player p = connect4.getPlayer(nickName);
        Player f = connect4.getPlayer("Maquina");

        if (ganador.equals(getNickName())) {
            connect4.registerRound(LocalDateTime.now(), p, f);
            p.addPoints(1);
        } else {
            connect4.registerRound(LocalDateTime.now(), f, p);
            p.addPoints(-1);
        }

        alert.showAndWait();
    }
    
    public void setGanador(boolean res){
        this.ganador = res;
        System.out.println(res);
    }
    
    public boolean getGanador(){
        return ganador;
    }
    
    private void mostrarAlertaVictoria(String ganador) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Victoria");
        alert.setHeaderText(null);
        alert.setContentText("¡" + ganador + " ha ganado esta partida!\n" +
                             "Victorias Jugador: " + victoriasJugador + "\n" +
                             "Victorias Máquina: " + victoriasMaquina);
        alert.showAndWait();
    }


    private void reiniciarJuego() throws IOException {
        boolean oscuroAux = esModoOscuro;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TableroMaquina.fxml"));
        Parent root = loader.load();

        TableroMaquinaController nuevoController = loader.getController();
        nuevoController.setPlayerNickname(nickName);
        nuevoController.setContadores(victoriasJugador, victoriasMaquina);

        Stage stage = (Stage) turnoDe.getScene().getWindow();
        stage.getScene().setRoot(root);
        stage.getIcons().add(icono);
        
        System.out.print(oscuroAux);

        
        if (!oscuroAux) {
            stage.getScene().getStylesheets().clear();
            stage.getScene().getStylesheets().add(getClass().getResource("/estilos/modoClaro.css").toExternalForm());
            System.out.print(oscuroAux);

            btnOscuro.setImage(new Image(getClass().getResourceAsStream("/resources/images/modoClaro.png")));
        }
        
        stage.show();
    }


    private boolean verificarVictoria(int columna, int fila) {
        int color = tableroVictoria[columna][fila];
        return color != -1 && (verificarDireccion(columna, fila, 1, 0, color) || 
                               verificarDireccion(columna, fila, 0, 1, color) || 
                               verificarDireccion(columna, fila, 1, 1, color) || 
                               verificarDireccion(columna, fila, 1, -1, color));
    }
    
    private void salir() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
        Parent root = loader.load();
        
        MenuController gameController = loader.getController();
        gameController.setPlayerNickname(nickName);
        
        Stage stage = (Stage) turnoDe.getScene().getWindow();

        stage.getScene().setRoot(root);
        stage.getIcons().add(icono);

        stage.show();
    }
    
    private boolean tableroLleno() {
        for (int i = 0; i < COLUMNAS; i++) {
            if (filasOcupadas[i] < FILAS) {
                return false;
            }
        }
        return true;
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
        scene.getStylesheets().clear();

        
        if (esModoOscuro) {
            scene.getStylesheets().add(getClass().getResource("/estilos/modoClaro.css").toExternalForm());
            btnOscuro.setImage(new Image(getClass().getResourceAsStream("/resources/images/modoClaro.png")));
        } else {
            scene.getStylesheets().add(getClass().getResource("/estilos/modoOscuro.css").toExternalForm());
            btnOscuro.setImage(new Image(getClass().getResourceAsStream("/resources/images/modoOscuro.png")));
        }

        esModoOscuro = !esModoOscuro;
    }
}