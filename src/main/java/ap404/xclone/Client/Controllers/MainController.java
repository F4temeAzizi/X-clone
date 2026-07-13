package ap404.xclone.Client.Controllers;
import ap404.xclone.Client.Managers.Navigation;
import ap404.xclone.Client.Managers.Session;
import ap404.xclone.Client.Managers.ThemeManager;
import ap404.xclone.Client.Utils.UserUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

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

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

            alert.setTitle("Log Out");
            alert.setHeaderText("Are you sure you want to log out?");

            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(ThemeManager.getThemeCss());
            dialogPane.getStylesheets().add(getClass().getResource("/css/alert.css").toExternalForm());
            dialogPane.getStyleClass().add("alert");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isEmpty() || result.get() != ButtonType.OK) return;

            try {
                Session.setClient(null);
                Session.setCurrentUser(null);
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

    @FXML public void goToBookmarks() { Navigation.loadBookmark(); }

    @FXML public void goToNotifications() {}

    @FXML public void  goToChat() {}

    @FXML public void goToSettings() { Navigation.loadSettings(); }

    @FXML public void showMoreOptions(ActionEvent e) {

        if(moreOptionsMenu.isShowing())
            moreOptionsMenu.hide();
        else {
            moreOptionsMenu.show(moreOptionsBtn, Side.TOP, 0, 0);
        }
    }

    public void updateUserProfile ()
    {
        UserUtil.loadUser(Session.getCurrentUser(),
                sidebarName, sidebarUsername,
                null, sidebarAvatar,
                null, null);
    }

    @FXML
    public  void  openPostPage()
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/post.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            ThemeManager.applyTheme(scene);
            stage.setScene(scene);
            stage.setTitle("Post");
            stage.setResizable(false);
            stage.showAndWait();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}