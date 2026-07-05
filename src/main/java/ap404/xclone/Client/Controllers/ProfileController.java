package ap404.xclone.Client.Controllers;

import ap404.xclone.Client.Managers.Session;
import ap404.xclone.Shared.Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ProfileController
{
    @FXML private Label nameLbl;
    @FXML private Label usernameLbl;
    @FXML private Label bioLbl;

    public void initialize ()
    {
        User user = Session.getCurrentUser();

        nameLbl.setText(user.getDisplayName());
        usernameLbl.setText("@" + user.getUsername());

        if (user.getBio() != null) {
            bioLbl.setText(user.getBio());
        }
    }
}
