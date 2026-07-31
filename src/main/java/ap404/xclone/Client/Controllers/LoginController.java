package ap404.xclone.Client.Controllers;

import ap404.xclone.Client.Client;
import ap404.xclone.Client.Managers.Navigation;
import ap404.xclone.Shared.DTO.response.Response;
import ap404.xclone.Shared.DTO.request.CredentialsRequest;
import ap404.xclone.Shared.DTO.enums.RequestType;
import ap404.xclone.Shared.DTO.enums.ResponseType;
import ap404.xclone.Shared.DTO.request.Request;
import ap404.xclone.Client.Managers.Session;
import ap404.xclone.Client.Utils.MessageUtil;
import ap404.xclone.Shared.Models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.io.IOException;

public class LoginController
{
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Button loginButton;

    @FXML
    private void handleLogin()
    {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isBlank() || password.isBlank())
        {
            MessageUtil.showError(errorLabel, "fields cannot be empty!");
            return;
        }

        try {

            Client client = new Client();

            CredentialsRequest credentialsRequest = new CredentialsRequest(username, password);
            client.sendRequest(new Request(RequestType.LOGIN, credentialsRequest));

            Response response = client.getResponse();

            if (response.getType().equals(ResponseType.LOGIN_SUCCESS)) {
                User user = (User) response.getBody();
                Session.setCurrentUser(user);
                Session.setClient(client);
                Navigation.navigate("main.fxml");
            }
            else {
                MessageUtil.showError(errorLabel, "invalid username or password");
            }

        } catch (Exception e) {
            MessageUtil.showError(errorLabel, "Cannot connect to server!");
        }
    }

    @FXML
    private void handleSignup() throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("/signup.fxml"));
        usernameField.getScene().setRoot(root);
    }
}