package ap404.xclone.Client.Controllers;

import ap404.xclone.Client.MessageUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController
{
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorMessageLabel;

    @FXML
    private void handleLogin()
    {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty())
        {
            MessageUtil.setErrorMessage(errorMessageLabel, "fields cannot be empty!");
            return;
        }
    }

    @FXML
    private void handleSignup() throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("/signup.fxml"));
        usernameField.getScene().setRoot(root);
    }
}