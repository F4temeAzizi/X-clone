package ap404.xclone.Client.Controllers;

import ap404.xclone.Client.Client;
import ap404.xclone.Client.Managers.Navigation;
import ap404.xclone.Client.Managers.Session;
import ap404.xclone.Client.Utils.TweetUtil;
import ap404.xclone.Shared.DTO.enums.RequestType;
import ap404.xclone.Shared.DTO.enums.ResponseType;
import ap404.xclone.Shared.DTO.request.Request;
import ap404.xclone.Shared.DTO.response.Response;
import ap404.xclone.Shared.Models.Tweet;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class ExploreController
{
    @FXML private VBox tweetContainer;
    @FXML private ScrollPane scrollPane;

    @FXML
    public void initialize()
    {
        loadTweets();
        scrollPane.layout();
        scrollPane.setVvalue(Navigation.getExploreScroll());
        scrollPane.vvalueProperty().addListener((obs, o, n) ->
                Navigation.setExploreScroll(n.doubleValue()));
    }

    private void loadTweets()
    {
        try
        {
            Client client = Session.getClient();

            Request request = new Request(RequestType.GET_ALL_TWEETS, Session.getCurrentUser().getId());

            client.sendRequest(request);

            Response response = client.getResponse();

            if (response.getType() == ResponseType.GET_TWEETS_SUCCESS)
            {
                List<Tweet> tweets = (List<Tweet>) response.getBody();
                TweetUtil.loadTweets(tweetContainer, tweets);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
