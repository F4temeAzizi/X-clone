package ap404.xclone.Client.Controllers;

import ap404.xclone.Client.Managers.Navigation;
import ap404.xclone.Client.Utils.UserUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;

public class ProfileController
{
    @FXML private Label nameLbl;
    @FXML private Label usernameLbl;
    @FXML private Label bioLbl;
    @FXML private ImageView avatarImage;
    @FXML private Region bannerRegion;

    public void initialize ()  { UserUtil.loadUser(nameLbl, usernameLbl, bioLbl, avatarImage, bannerRegion); }

    @FXML public void goToEditProfile() { Navigation.loadEditProfile(); }
}
