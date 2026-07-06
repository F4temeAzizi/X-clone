package ap404.xclone.Client.Controllers;

import ap404.xclone.Client.Client;
import ap404.xclone.Client.Utils.MessageUtil;
import ap404.xclone.Shared.*;
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
            MessageUtil.setErrorMessage(errorLabel, "All fields are required!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            MessageUtil.setErrorMessage(errorLabel, "Passwords do not match!");
            return;
        }

        try {

            Client client = new Client();

            SignupRequest signupRequest = new SignupRequest(name, username, password, email);
            client.sendRequest(new Request(RequestType.SIGNUP, signupRequest));

            Response response = client.getResponse();

            if (response.getType().equals(ResponseType.SIGNUP_SUCCESS)) {
                MessageUtil.setErrorMessage(errorLabel, "Sign up succeed!");
            }
            else {
                MessageUtil.setErrorMessage(errorLabel, "Sign up failed!");
            }

        } catch (Exception e) {
            MessageUtil.setErrorMessage(errorLabel, "Cannot connect to server!");
        }
    }

    @FXML
    private void goToLogin() throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
        nameField.getScene().setRoot(root);
    }
}