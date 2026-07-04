package ap404.xclone.Client.Controllers;

import ap404.xclone.Shared.Models.Tweet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainController
{
    @FXML private VBox tweetContainer;

    @FXML
    public void initialize()
    {
        try
        {
            addTweet(new Tweet("Ali", "@ali", "Hello World!"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void addTweet(Tweet tweet) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Tweet.fxml"));

        HBox root = loader.load();

        TweetController controller = loader.getController();
        controller.setTweet(tweet);

        tweetContainer.getChildren().add(root);
    }
}