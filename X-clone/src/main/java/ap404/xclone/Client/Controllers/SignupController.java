package ap404.xclone.Client.Controllers;

import ap404.xclone.Client.MessageUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import java.io.IOException;

public class SignupController
{
    @FXML private TextField nameField;
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label errorLabel;

    @FXML
    private void handleSignup()
    {
        if (nameField.getText().isBlank()
                || usernameField.getText().isBlank()
                || emailField.getText().isBlank()
                || passwordField.getText().isBlank()
                || confirmPasswordField.getText().isBlank()) {
            MessageUtil.setErrorMessage(errorLabel, "All fields are required!");
            return;
        }
    }

    @FXML
    private void goToLogin() throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
        nameField.getScene().setRoot(root);
    }
}