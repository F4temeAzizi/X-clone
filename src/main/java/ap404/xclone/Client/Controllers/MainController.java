package ap404.xclone.Client.Controllers;
import ap404.xclone.Client.Managers.Navigation;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;


public class MainController
{
    @FXML private StackPane center;

    @FXML
    public void initialize()
    {
        Navigation.setCenter(center);
        Navigation.loadHome();
    }

    @FXML public void goToProfile() { Navigation.loadProfile(); }

    @FXML public void goToHome() { Navigation.loadHome(); }

    @FXML public void goToExplore() {}

    @FXML public void goToBookmarks() {}

    @FXML public void goToNotifications() {}

    @FXML public void  goToChat() {}

    @FXML public void goToSettings() {}
}