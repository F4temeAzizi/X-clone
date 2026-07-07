package ap404.xclone.Client.Controllers;
import ap404.xclone.Client.Managers.Navigation;
import ap404.xclone.Client.Utils.UserUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class MainController
{
    @FXML private StackPane center;
    @FXML private ImageView sidebarAvatar;
    @FXML private Label sidebarName;
    @FXML private Label sidebarUsername;

    @FXML
    public void initialize()
    {
        Navigation.setCenter(center);
        Navigation.setMainController(this);
        updateUserProfile();
        Navigation.loadHome();
    }

    @FXML public void goToProfile() { Navigation.loadProfile(); }

    @FXML public void goToHome() { Navigation.loadHome(); }

    @FXML public void goToExplore() {}

    @FXML public void goToBookmarks() {}

    @FXML public void goToNotifications() {}

    @FXML public void  goToChat() {}

    @FXML public void goToSettings() {}

    public void updateUserProfile ()
    {
        UserUtil.loadUser(sidebarName, sidebarUsername, null, sidebarAvatar, null, null);
    }
}