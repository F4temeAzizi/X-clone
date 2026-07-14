package ap404.xclone.Client.Controllers;

import ap404.xclone.Client.Client;
import ap404.xclone.Client.Managers.Navigation;
import ap404.xclone.Client.Managers.Session;
import ap404.xclone.Client.Utils.TweetUtil;
import ap404.xclone.Shared.DTO.enums.RequestType;
import ap404.xclone.Shared.DTO.enums.ResponseType;
import ap404.xclone.Shared.DTO.request.GetBookmarkedTweetsRequest;
import ap404.xclone.Shared.DTO.request.Request;
import ap404.xclone.Shared.DTO.response.Response;
import ap404.xclone.Shared.Models.Tweet;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import java.util.List;

public class BookmarkController
{
    @FXML VBox tweetContainer;
    @FXML ScrollPane scrollPane;

    @FXML
    public void initialize()
    {
        try
        {
            Client client = new Client();

            client.sendRequest(new Request(RequestType.GET_BOOKMARKED_TWEETS,
                    new GetBookmarkedTweetsRequest(Session.getCurrentUser().getId(), Session.getCurrentUser().getId())));

            Response response = client.getResponse();

            if (response.getType() == ResponseType.GET_BOOKMARKED_TWEETS_SUCCESS)
            {
                List<Tweet> tweets = (List<Tweet>) response.getBody();
                TweetUtil.loadTweets(tweetContainer, tweets);
            }
            scrollPane.layout();
            scrollPane.setVvalue(Navigation.getBookmarkScroll());
            scrollPane.vvalueProperty().addListener((obs, o, n) ->
                    Navigation.setBookmarkScroll(n.doubleValue()));
        }
        catch (Exception e)
        {
            System.err.println("Failed to load bookmarked tweets: " + e.getMessage());
        }
    }
}
