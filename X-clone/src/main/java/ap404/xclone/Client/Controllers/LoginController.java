package ap404.xclone.Client.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;

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
    private void handleSignup()
    {
    }
}