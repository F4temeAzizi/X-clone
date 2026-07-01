package ap404.xclone.Client.Controllers;

import ap404.xclone.Client.Client;
import ap404.xclone.Client.MessageUtil;
import ap404.xclone.Shared.*;
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
    @FXML private Label errorLabel;

    @FXML
    private void handleLogin()
    {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isBlank() || password.isBlank())
        {
            MessageUtil.setErrorMessage(errorLabel, "fields cannot be empty!");
            return;
        }

        try {

            Client client = new Client();

            LoginRequest loginRequest = new LoginRequest(username, password);
            client.sendRequest(new Request(RequestType.LOGIN, loginRequest));

            Response response = client.getResponse();

            if (response.getType().equals(ResponseType.LOGIN_SUCCESS)) {
                navigateToMain();
            }
            else {
                MessageUtil.setErrorMessage(errorLabel, "invalid username or password!");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void handleSignup() throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("/signup.fxml"));
        usernameField.getScene().setRoot(root);
    }

    private void navigateToMain() {

    }
}