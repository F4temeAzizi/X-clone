package ap404.xclone.Client.Controllers;

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
            setErrorMessage("fields can't be empty!");
            return;
        }
    }

    private void setErrorMessage(String message)
    {
        if (message.isEmpty()) errorMessageLabel.setVisible(false);
        else
        {
            errorMessageLabel.setText(message);
            errorMessageLabel.setVisible(true);
        }
    }

    @FXML
    private void handleSignup() throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("/signup.fxml"));
        usernameField.getScene().setRoot(root);
    }
}