package ap404.xclone.Client.Managers;

import ap404.xclone.Client.Controllers.HomeController;
import ap404.xclone.Client.Controllers.MainController;
import ap404.xclone.Client.XApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class Navigation
{
    private static StackPane center;
    private static MainController mainController;
    private static HomeController homeController;

    public static void loadHome() { load("home.fxml"); }

    public static void loadProfile() { load("profile.fxml"); }

    public static void loadEditProfile() { load("edit-profile.fxml"); }

    public static void setMainController(MainController controller) { mainController = controller; }

    public static MainController getMainController() { return mainController; }

    public static void setHomeController (HomeController controller) { homeController = controller;}

    public static HomeController getHomeController() { return homeController; }

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

    public static void navigate(String fxml) throws IOException{
        Parent root = FXMLLoader.load(Navigation.class.getResource("/" + fxml));
        XApplication.getPrimaryStage().getScene().setRoot(root);
    }

}