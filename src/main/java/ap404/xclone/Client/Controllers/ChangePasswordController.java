package ap404.xclone.Client.Controllers;

import ap404.xclone.Client.Client;
import ap404.xclone.Client.Managers.Navigation;
import ap404.xclone.Client.Managers.Session;
import ap404.xclone.Client.Utils.MessageUtil;
import ap404.xclone.Shared.DTO.enums.RequestType;
import ap404.xclone.Shared.DTO.enums.ResponseType;
import ap404.xclone.Shared.DTO.request.ChangePasswordRequest;
import ap404.xclone.Shared.DTO.request.Request;
import ap404.xclone.Shared.DTO.response.Response;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ChangePasswordController
{
    @FXML private TextField currentPasswordField;
    @FXML private TextField newPasswordField;
    @FXML private TextField confirmNewPasswordField;
    @FXML private Label errorLabel;

    @FXML public void handleBack() { Navigation.loadSettings(); }
    @FXML public void cancel () { Navigation.loadSettings(); }

    @FXML public void handleChangePassword() {

        errorLabel.setVisible(false);
        errorLabel.setText("");

        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmNewPassword = confirmNewPasswordField.getText();

        if(currentPassword.isBlank() || newPassword.isBlank() || confirmNewPassword.isBlank()) {
            MessageUtil.showError(errorLabel,"All fields are required!");
            return;
        }

        if (!newPassword.equals(confirmNewPassword)) {
            MessageUtil.showError(errorLabel, "Passwords do not match!");
            return;
        }

        if (newPassword.equals(currentPassword)) {
            MessageUtil.showError(errorLabel, "New password must be different from the current password.");
            return;
        }

        try {

            Client client = Session.getClient();

            ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(
                    Session.getCurrentUser().getId(),
                    currentPassword,
                    newPassword
            );

            client.sendRequest(new Request(RequestType.CHANGE_PASSWORD, changePasswordRequest));

            Response response = client.getResponse();

            if (response.getType() == ResponseType.CHANGE_PASSWORD_SUCCESS) {

                MessageUtil.showSuccess(errorLabel, "Password changed successfully.");

                currentPasswordField.clear();
                newPasswordField.clear();
                confirmNewPasswordField.clear();
            }
            else {
                MessageUtil.showError(errorLabel ,"Changing password failed! ");
            }

        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.showError(errorLabel, "Connection to server failed.");
        }
    }
}
