/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxmlapplication;

import connect4.Connect4;
import connect4.Player;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class PerfilController implements Initializable {

    @FXML
    private Text usuario;
    @FXML
    private Text contraseña;
    @FXML
    private Text correo;
    @FXML
    private Text fechaNacimiento;

    private String playerNickname;
    @FXML
    private ImageView avatarImag;
    /**
     * Initializes the controller class.
     */
    
    Image icono = new Image(getClass().getResourceAsStream("/resources/images/4raya.png"));

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // TODO
    }    
    
    public void initializeWithNickname(String nickname) {
        this.playerNickname = nickname;
        Connect4 connect4 = Connect4.getInstance();
        Player p = connect4.getPlayer(nickname); 

        
         
        usuario.setText(p.getNickName());
        contraseña.setText( p.getPassword());
        correo.setText(p.getEmail());
        fechaNacimiento.setText(p.getBirthdate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        avatarImag.setImage(p.getAvatar());
        
    }

    @FXML
    private void modificarFecha(ActionEvent event) {
        Connect4 connect4 = Connect4.getInstance();
        Player  p = connect4.getPlayer(usuario.getText());
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(p.getBirthdate()); 

        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Modificar Fecha de Nacimiento");
        alert.setHeaderText("Selecciona una nueva fecha de nacimiento");
        alert.setContentText("Por favor, selecciona la nueva fecha de nacimiento:");

        
        VBox vbox = new VBox(datePicker);
        vbox.setSpacing(10);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setContent(vbox);

        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            
            LocalDate newBirthdate = datePicker.getValue();

            if (newBirthdate != null && validarEdad(newBirthdate)) {
                
                p.setBirthdate(newBirthdate);
                fechaNacimiento.setText(p.getBirthdate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

                
                showAlert("Fecha Modificada", "La fecha de nacimiento ha sido actualizada correctamente a: " + newBirthdate);

                
            } else {
                showAlert("Error", "Debes tener al menos 12 años para poder jugar.");
            }
        }
    }

    @FXML
    private void modificarCorreo(ActionEvent event) {
        Connect4 connect4 = Connect4.getInstance();
        Player  p = connect4.getPlayer(usuario.getText());
        TextInputDialog dialog = new TextInputDialog(usuario.getText());
        dialog.setTitle("Modificar Correo");
        dialog.setHeaderText("Modificar el correo");
        dialog.setContentText("Introduce el correo:");

        
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newCorreo-> {
            if (newCorreo.isEmpty() || !validarEmail(newCorreo)) {
                showAlert("Error", "Correo electrónico inválido.");
            } else{
                

                p.setEmail(newCorreo);
                correo.setText(newCorreo);

                showAlert("Éxito", "El correo ha sido modificado con éxito.");
            }
        });
    }

    @FXML
    private void modificarPassword(ActionEvent event) {
        Connect4 connect4 = Connect4.getInstance();
        Player  p = connect4.getPlayer(usuario.getText());
        TextInputDialog dialog = new TextInputDialog(usuario.getText());
        dialog.setTitle("Modificar Password");
        dialog.setHeaderText("Modificar el password");
        dialog.setContentText("Introduce el nuevo password:");

        
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newPassword -> {
            if (newPassword.isEmpty() || !validarPassword(newPassword)) {
                showAlert("Error","La contraseña debe tener entre 8 y 20 caracteres, incluir mayúsculas, minúsculas, un dígito y un carácter especial.");
            } else{
                
                p.setPassword(newPassword);
                contraseña.setText(newPassword);

                showAlert("Éxito", "El passsword ha sido modificado con éxito.");
                
            }
        });
    }
    
    @FXML
    private void modificarAvatar(ActionEvent event) {
        Connect4 connect4 = Connect4.getInstance();
        Player  p = connect4.getPlayer(usuario.getText());
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Avatar");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
               
                Image newAvatar = new Image(selectedFile.toURI().toString());

                
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setTitle("Confirmar Cambio de Avatar");
                confirmAlert.setHeaderText("¿Deseas establecer esta imagen como tu nuevo avatar?");
                ImageView imageView = new ImageView(newAvatar);
                imageView.setFitHeight(100);
                imageView.setFitWidth(100);
                confirmAlert.setGraphic(imageView);

                Optional<ButtonType> result = confirmAlert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    
                    p.setAvatar(newAvatar);
                    avatarImag.setImage(newAvatar);
                    

                    
                    showAlert("Avatar Actualizado", "Tu avatar ha sido actualizado correctamente.");

                    
                }
            } catch (Exception e) {
                showAlert("Error", "No se pudo cargar la imagen seleccionada. Por favor, inténtalo de nuevo.");
            }
        } else {
            
            showAlert("Operación Cancelada", "No se seleccionó ninguna imagen.");
        }
    }


    @FXML
    private void regresar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
            Scene gameScene = new Scene(loader.load());

            
            MenuController gameController = loader.getController();
            
            gameController.setPlayerNickname(this.playerNickname);

            
            Stage stage = new Stage();
            stage.setScene(gameScene);
            stage.getIcons().add(icono);
            stage.show();

            
            Stage currentStage = (Stage) usuario.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
    
    
}
