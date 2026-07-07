package ap404.xclone.Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class XApplication extends Application
{
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception
    {
        primaryStage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("𝕏");
        stage.setScene(scene);

        stage.setMaximized(true);      
        stage.setResizable(false);

        stage.show();
    }

    public static Stage getPrimaryStage() { return primaryStage; }

    public static void main(String[] args) {
        launch(args);
    }
}