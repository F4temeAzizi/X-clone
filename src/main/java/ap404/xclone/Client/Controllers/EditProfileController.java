package ap404.xclone.Client.Controllers;

import ap404.xclone.Client.Managers.Session;
import ap404.xclone.Shared.Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class EditProfileController
{
    @FXML private TextField nameField;
    @FXML private TextField usernameField;
    @FXML private TextArea bioField;

    public  void initialize()
    {
        User user = Session.getCurrentUser();

        nameField.setText(user.getDisplayName());
        usernameField.setText(user.getUsername());
        if (user.getBio() != null) {
            bioField.setText(user.getBio());
        }
    }
}
