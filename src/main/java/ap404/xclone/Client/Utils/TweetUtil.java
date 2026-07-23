package ap404.xclone.Client.Utils;

import ap404.xclone.Client.Controllers.TweetController;
import ap404.xclone.Shared.Models.Tweet;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.List;

public class TweetUtil
{
    public static TweetController addTweet (Pane container, Tweet tweet, int index) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(TweetUtil.class.getResource("/Tweet.fxml"));

        HBox root = loader.load();

        TweetController controller = loader.getController();
        controller.setTweet(tweet);

        if (index < 0){
            container.getChildren().add(root);
        }
        else container.getChildren().add(0, root);

        return controller;
    }

    public static void loadTweets (VBox container, List<Tweet> tweets) throws Exception
    {
        container.getChildren().clear();
        for (Tweet tweet: tweets)
        {
            addTweet(container, tweet, -1);
        }
    }
}
