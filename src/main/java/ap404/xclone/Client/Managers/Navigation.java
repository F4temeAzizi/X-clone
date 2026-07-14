package ap404.xclone.Client.Managers;

import ap404.xclone.Client.Controllers.ExploreController;
import ap404.xclone.Client.Controllers.HomeController;
import ap404.xclone.Client.Controllers.MainController;
import ap404.xclone.Client.XApplication;
import ap404.xclone.Shared.Models.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class Navigation
{
    private static StackPane center;
    private static MainController mainController;
    private static HomeController homeController;
    private static ExploreController exploreController;
    private static User selectedUser;

    private static double homeScroll = 0;
    private static double exploreScroll = 0;
    private static double bookmarkScroll = 0;
    private static double profileScroll = 0;
    private static String profileTab = "posts";

    private static String composeText = "";

    public static void loadHome() { load("home.fxml"); }
    public static void loadProfile() { load("profile.fxml"); }
    public static void loadEditProfile() { load("edit-profile.fxml"); }
    public static void loadExplore() { load("explore.fxml"); }
    public static void loadBookmark() { load("bookmark.fxml"); }
    public static void loadOthersProfile() { load("others-profile.fxml"); }
    public static void loadSettings() { load("settings.fxml"); }
    public static void loadTheme() { load("theme.fxml"); }
    public static void loadChangePassword() { load("change-password.fxml"); }
    public static void loadPrivacy() { load("privacy.fxml"); }
    public static void loadDeleteAccount() { load("delete-account.fxml"); }

    public static User getSelectedUser() { return selectedUser; }
    public static void setSelectedUser(User selectedUser) { Navigation.selectedUser = selectedUser;}

    public static void setMainController(MainController controller) { mainController = controller; }
    public static MainController getMainController() { return mainController; }

    public static void setHomeController(HomeController controller) { homeController = controller;}
    public static HomeController getHomeController() { return homeController; }

    public static void setExploreController(ExploreController controller) { exploreController = controller; }
    public static ExploreController getExploreController() { return exploreController; }

    public static double getHomeScroll() { return homeScroll; }
    public static void setHomeScroll(double value) { homeScroll = value; }

    public static double getExploreScroll() { return exploreScroll; }
    public static void setExploreScroll(double value) { exploreScroll = value; }

    public static double getBookmarkScroll() { return bookmarkScroll; }
    public static void setBookmarkScroll(double value) { bookmarkScroll = value; }

    public static double getProfileScroll() { return profileScroll; }
    public static void setProfileScroll(double value) { profileScroll = value; }

    public static String getProfileTab() { return profileTab; }
    public static void setProfileTab(String tab) { profileTab = tab; }

    public static String getComposeText() { return composeText; }
    public static void setComposeText(String text) { composeText = text; }

    public static void setCenter(StackPane centerPane)
    {
        center = centerPane;
    }

    public static void load(String fxml)
    {
        try
        {
            Parent root = FXMLLoader.load(Navigation.class.getResource("/" + fxml));
            center.getChildren().setAll(root);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void navigate(String fxml) throws IOException {
        Parent root = FXMLLoader.load(Navigation.class.getResource("/" + fxml));
        XApplication.getPrimaryStage().getScene().setRoot(root);
    }
}