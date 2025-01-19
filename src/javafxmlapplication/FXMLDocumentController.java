/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxmlapplication;

import connect4.Connect4;
import connect4.Player;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 *
 * @author jsoler
 */
public class FXMLDocumentController implements Initializable {
    private Label labelMessage;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private DatePicker birthDatePicker;
    @FXML
    private Button registerButton;
    @FXML
    private Button avatarButton;
    @FXML
    private Label avatarLabel;
    
   
    private String avatarPath = "/resources/images/default_avatar.png"; 
    
    @FXML
    private ImageView avatarImageView;
    Image icono = new Image(getClass().getResourceAsStream("/resources/images/4raya.png"));

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        avatarImageView.setImage(new Image(getClass().getResource(avatarPath).toExternalForm()));
        avatarButton.setOnAction(event -> seleccionarAvatar());
    }

    @FXML
    private void seleccionarAvatar() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Seleccionar Avatar");
    fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
    );
    Stage stage = (Stage) avatarButton.getScene().getWindow();
    var file = fileChooser.showOpenDialog(stage);

    if (file != null) {
        avatarPath = file.toURI().toString(); 
        avatarLabel.setText("Avatar seleccionado: " + file.getName());

       
        avatarImageView.setImage(new Image(avatarPath));
    }
}

    private boolean validarUsername(String username) {
        return username.matches("^[a-zA-Z0-9_-]{6,15}$");
    }

    private boolean validarEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@[\\w-]+\\.[a-zA-Z]{2,}$";
        return Pattern.matches(emailRegex, email);
    }

    private boolean validarPassword(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%&*()\\-+=])[A-Za-z\\d!@#$%&*()\\-+=]{8,20}$");
    }

    private boolean validarEdad(LocalDate birthdate) {
        if (birthdate == null) return false;
        return Period.between(birthdate, LocalDate.now()).getYears() >= 12;
    }

    private void guardarUsuario(String username, String email, String password, LocalDate birthdate, String avatarPath) {
        System.out.printf("Usuario: %s, Email: %s, Contraseña: %s, Fecha de Nacimiento: %s, Avatar: %s%n",
                username, email, password, birthdate, avatarPath);
    }

    private void limpiarFormulario() {
        usernameField.clear();
        emailField.clear();
        passwordField.clear();
        birthDatePicker.setValue(null);
        avatarLabel.setText("Avatar predeterminado seleccionado");
        avatarPath = "default_avatar.png";
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registro");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    
    private void mostrarAlerta (String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void autenticarUser(ActionEvent event) {
        try {
            Window window = registerButton.getScene().getWindow(); 
            if (window instanceof Stage) {
                ((Stage) window).close();
            }

            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Autenticarse.fxml")); 
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Autenticación");
            stage.setScene(new Scene(root));
            stage.getIcons().add(icono);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    
    }

    @FXML
    private void registrarUser(ActionEvent event) {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        LocalDate birthdate = birthDatePicker.getValue();

       
        if (!validarUsername(username)) {
            mostrarAlerta("Nombre de usuario inválido. Debe tener entre 6 y 15 caracteres y no contener espacios.");
            return;
        }

        if (!validarEmail(email)) {
            mostrarAlerta("Correo electrónico inválido.");
            return;
        }
        
        if (!validarPassword(password)) {
            mostrarAlerta("La contraseña debe tener entre 8 y 20 caracteres, incluir mayúsculas, minúsculas, un dígito y un carácter especial.");
            return;
        }

        if (!validarEdad(birthdate)) {
            mostrarAlerta("Debes tener al menos 12 años para registrarte.");
            return;
        }

      
        Connect4 connect4 = Connect4.getInstance();
        if(!connect4.existsNickName(username)){
        Player newPlayer = connect4.registerPlayer(username, email, password, birthdate, 0);
            if(newPlayer != null) {
                mostrarAlerta("¡Usuario registrado exitosamente!");
            } else {
                mostrarAlerta("Error", "Hubo un error al registrar al jugador.");
            }
        
        limpiarFormulario();
        }else{
            mostrarAlerta("Nombre de usuario ya elegido, porfavor escoja otro");
        }
    }

    @FXML
    private void salir(ActionEvent event) {
        Platform.exit();
    }
}
