package ap404.xclone.Client.Managers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class Navigation
{
    private static StackPane center;

    public static void loadHome() { load("home.fxml"); }

    public static void loadProfile() { load("profile.fxml"); }

    public static void loadEditProfile() { load("edit-profile.fxml"); }

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
}