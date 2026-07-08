package ap404.xclone.Client.Controllers;

import ap404.xclone.Client.Utils.TweetUtil;
import ap404.xclone.Shared.Models.Tweet;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class ExploreController
{
    @FXML private VBox tweetContainer;

    @FXML
    public void initialize() { loadTweets(); }

    private void loadTweets()
    {
        try
        {
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
