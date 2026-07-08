package ap404.xclone.Client.Controllers;
import ap404.xclone.Client.Managers.Navigation;
import ap404.xclone.Client.Utils.UserUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainController
{
    @FXML private Button moreOptionsBtn;
    @FXML private StackPane center;
    @FXML private ImageView sidebarAvatar;
    @FXML private Label sidebarName;
    @FXML private Label sidebarUsername;

    private ContextMenu moreOptionsMenu;

    @FXML
    public void initialize()
    {
        Navigation.setCenter(center);
        Navigation.setMainController(this);
        updateUserProfile();
        Navigation.loadHome();

        moreOptionsMenu = new ContextMenu();
        moreOptionsMenu.getStyleClass().add("x-menu");
        MenuItem logoutItem = new MenuItem("Log Out");

        logoutItem.setOnAction(e -> {
            try {
                Navigation.navigate("login.fxml");
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        });

        moreOptionsMenu.getItems().add(logoutItem);
    }

    @FXML public void goToProfile() { Navigation.loadProfile(); }

    @FXML public void goToHome() { Navigation.loadHome(); }

    @FXML public void goToExplore() { Navigation.loadExplore(); }

    @FXML public void goToBookmarks() {}

    @FXML public void goToNotifications() {}

    @FXML public void  goToChat() {}

    @FXML public void goToSettings() {}

    @FXML public void showMoreOptions(ActionEvent e) {

        if(moreOptionsMenu.isShowing())
            moreOptionsMenu.hide();
        else {
            moreOptionsMenu.show(moreOptionsBtn, Side.TOP, 0, 0);
        }
    }

    public void updateUserProfile ()
    {
        UserUtil.loadUser(sidebarName, sidebarUsername, null, sidebarAvatar, null, null);
    }
}