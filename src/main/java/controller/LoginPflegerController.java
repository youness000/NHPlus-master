package controller;

import datastorage.CareGiverDAO;
import datastorage.DAOFactory;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class LoginPflegerController {
    // Strings which hold css elements to easily re-use in the application
    protected
    String successMessage = String.format("-fx-text-fill: #2ecc71;");
    String errorMessage = String.format("-fx-text-fill: #e74c3c;");
    String errorStyle = String.format("-fx-border-color: #e74c3c; -fx-border-width: 2; -fx-border-radius: 5;");
    String successStyle = String.format("-fx-border-color: #27ae60; -fx-border-width: 2; -fx-border-radius: 5;");

    // Import the application's controls
    @FXML
    private Label invalidLoginCredentials;
    @FXML
    private Label invalidSignupCredentials;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField loginUsernameTextField;
    @FXML
    private TextField loginPasswordPasswordField;
    @FXML private Button closeButton;

    @FXML
    private TextField txtFieldCompanyName;
    @FXML
    public void initialize() {
        //unfocus pathField
        Platform.runLater( () -> txtFieldCompanyName.setFocusTraversable(false) );
    }
    // Creation of methods which are activated on events in the forms
    @FXML
    protected void onCancelButtonClick() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void onLoginButtonClick() throws SQLException, IOException {
        if (loginUsernameTextField.getText().isBlank() || loginPasswordPasswordField.getText().isBlank()) {
            invalidLoginCredentials.setText("Die Login-Felder sind Pflichtfelder!");
            invalidLoginCredentials.setStyle(errorMessage);

            if (loginUsernameTextField.getText().isBlank()) {
                loginUsernameTextField.setStyle(errorStyle);
            } else if (loginPasswordPasswordField.getText().isBlank()) {
                loginPasswordPasswordField.setStyle(errorStyle);
            }
        } else {
            CareGiverDAO dao = DAOFactory.getDAOFactory().createCareGiverDAO();
            int count = dao.nurseCheckLogin(loginUsernameTextField.getText(), loginPasswordPasswordField.getText());
            if (count > 0) {
                invalidLoginCredentials.setText("Anmeldung erfolgreich!");
                invalidLoginCredentials.setStyle(successMessage);
                loginUsernameTextField.setStyle(successStyle);
                loginPasswordPasswordField.setStyle(successStyle);

                // Open Home Window
                Parent part = FXMLLoader.load(getClass().getResource("/MainWindowView.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(part);
                stage.setScene(scene);
                stage.show();
                cancelButton.fire();
            } else {
                invalidLoginCredentials.setText("Benutzername oder Passwort ist/sind falsch eingegeben");
                invalidLoginCredentials.setStyle(errorMessage);
                loginUsernameTextField.setStyle(errorStyle);
                loginPasswordPasswordField.setStyle(errorStyle);
            }
        }
    }

}
