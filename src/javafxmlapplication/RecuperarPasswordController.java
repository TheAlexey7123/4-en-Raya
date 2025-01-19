/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import connect4.Connect4;
import connect4.Player;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class RecuperarPasswordController implements Initializable {

    @FXML
    private TextField usuarioField;
    @FXML
    private TextField correoField;
    
    Image icono = new Image(getClass().getResourceAsStream("/resources/images/4raya.png"));


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void recuperarPassword(ActionEvent event) {
        Connect4 connect4 = Connect4.getInstance();
        Player p = connect4.getPlayer(usuarioField.getText());
        if(p != null && p.getEmail().equals(correoField.getText())){
            String codigoSeguridad = String.valueOf((int)(Math.random() * 9000 + 1000));
                    System.out.println("Código para recuperar password " + codigoSeguridad);

                    // Solicitar código al usuario
                    TextInputDialog codigoDialog = new TextInputDialog();
                    codigoDialog.setTitle("Verificación de seguridad");
                    codigoDialog.setHeaderText("Se ha enviado un código a su correo");
                    codigoDialog.setContentText("Ingrese el código recibido:");

                    Optional<String> codigoIngresado = codigoDialog.showAndWait();
                    if (codigoIngresado.isPresent() && codigoIngresado.get().equals(codigoSeguridad)) {
                        // Mostrar contraseña
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Recuperación exitosa");
                        alert.setHeaderText("Su contraseña es:");
                        alert.setContentText(p.getPassword());
                        alert.showAndWait();
                    } else {
                        mostrarError("Código incorrecto", "El código ingresado no coincide con el enviado.");
                    }
                } else {
                    mostrarError("Usuario o correo no válidos", "No se encontró un usuario con los datos proporcionados.");
                
        }
        
    }

    @FXML
    private void regresar(ActionEvent event) {
        try {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Autenticarse.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.getIcons().add(icono);

            stage.show();
            
            Stage currentStage = (Stage) usuarioField.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    private void mostrarError(String titulo, String mensaje) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(titulo);
    alert.setHeaderText(null);
    alert.setContentText(mensaje);
    alert.showAndWait();
}
}
