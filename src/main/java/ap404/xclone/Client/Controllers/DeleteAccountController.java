package ap404.xclone.Client.Controllers;

import ap404.xclone.Client.Client;
import ap404.xclone.Client.Managers.Navigation;
import ap404.xclone.Client.Managers.Session;
import ap404.xclone.Client.Utils.MessageUtil;
import ap404.xclone.Shared.DTO.enums.RequestType;
import ap404.xclone.Shared.DTO.enums.ResponseType;
import ap404.xclone.Shared.DTO.request.CredentialsRequest;
import ap404.xclone.Shared.DTO.request.Request;
import ap404.xclone.Shared.DTO.response.Response;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class DeleteAccountController
{
    @FXML private Label errorLabel;
    @FXML private TextField confirmPasswordField;


    @FXML public void handleBack() { Navigation.loadSettings(); }
    @FXML public void cancel () { Navigation.loadSettings(); }

    @FXML public void handleDeleteAccount() {

        errorLabel.setText("");
        errorLabel.setVisible(false);

        String password = confirmPasswordField.getText().trim();

        if(password.isEmpty()) {
            MessageUtil.showError(errorLabel, "Please enter your password");
            return;
        }

        try {

            Client client = Session.getClient();

            CredentialsRequest credentialsRequest = new CredentialsRequest(
                    Session.getCurrentUser().getUsername(),
                    password
            );

            client.sendRequest(new Request(RequestType.DELETE_ACCOUNT, credentialsRequest));

            Response response = client.getResponse();

            if (response.getType() == ResponseType.DELETE_ACCOUNT_SUCCESS) {
                Session.setClient(null);
                Session.setCurrentUser(null);
                Navigation.navigate("login.fxml");
            }
            else {
                MessageUtil.showError(errorLabel, "Couldn't delete your account");
            }
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.showError(errorLabel, "Connection to server failed.");
        }
    }
}
