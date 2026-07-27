package ap404.xclone.Client.Controllers;

import ap404.xclone.Client.Client;
import ap404.xclone.Shared.DTO.response.Response;
import ap404.xclone.Shared.DTO.request.Request;
import ap404.xclone.Shared.DTO.request.SignupRequest;
import ap404.xclone.Shared.DTO.enums.RequestType;
import ap404.xclone.Shared.DTO.enums.ResponseType;
import ap404.xclone.Client.Utils.MessageUtil;
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

        String name = nameField.getText();
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (name.isBlank()
                || username.isBlank()
                || email.isBlank()
                || password.isBlank()
                || confirmPassword.isBlank()) {
            MessageUtil.showError(errorLabel, "All fields are required!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            MessageUtil.showError(errorLabel, "Passwords do not match!");
            return;
        }

        try {

            Client client = new Client();

            SignupRequest signupRequest = new SignupRequest(name, username, password, email);
            client.sendRequest(new Request(RequestType.SIGNUP, signupRequest));

            Response response = client.getResponse();

            if (response.getType().equals(ResponseType.SIGNUP_SUCCESS)) {
                MessageUtil.showSuccess(errorLabel, "Sign up succeed!");
            }
            else {
                MessageUtil.showError(errorLabel, "Sign up failed!");
            }

        } catch (Exception e) {
            MessageUtil.showError(errorLabel, "Cannot connect to server!");
        }
    }

    @FXML
    private void goToLogin() throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
        nameField.getScene().setRoot(root);
    }
}