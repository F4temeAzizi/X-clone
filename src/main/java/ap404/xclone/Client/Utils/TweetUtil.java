package ap404.xclone.Client.Utils;

import ap404.xclone.Client.Controllers.TweetController;
import ap404.xclone.Shared.Models.Tweet;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TweetUtil
{
    public static void addTweet (VBox container, Tweet tweet) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(TweetUtil.class.getResource("/Tweet.fxml"));

        HBox root = loader.load();

        TweetController controller = loader.getController();
        controller.setTweet(tweet);

        container.getChildren().add(root);
    }
}
