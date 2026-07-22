package ap404.xclone.Client.Controllers;

import ap404.xclone.Client.Client;
import ap404.xclone.Client.Managers.Navigation;
import ap404.xclone.Client.Managers.Session;
import ap404.xclone.Client.Utils.TweetUtil;
import ap404.xclone.Shared.DTO.enums.RequestType;
import ap404.xclone.Shared.DTO.enums.ResponseType;
import ap404.xclone.Shared.DTO.request.Request;
import ap404.xclone.Shared.DTO.request.SearchTweetsRequest;
import ap404.xclone.Shared.DTO.request.ShowHashtagRequest;
import ap404.xclone.Shared.DTO.response.Response;
import ap404.xclone.Shared.Models.Tweet;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class ExploreController
{
    @FXML private VBox tweetContainer;
    @FXML private ScrollPane scrollPane;
    @FXML private TextField searchField;

    @FXML
    public void initialize()
    {
        Navigation.setExploreController(this);
        String hashtag = Navigation.getSelectedHashtag();
        if (hashtag != null)
        {
            searchField.setText("#" + hashtag);
            showHashtag(hashtag);
            Navigation.setSelectedHashtag(null);
        }
        else loadTweets();

        searchField.textProperty().addListener((obs, o, n) -> {
            if (n.isBlank()) loadTweets();
            else if (n.startsWith("#")) showHashtag(n.substring(1));
            else searchTweets(n);
        });

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

    private void searchTweets(String keyword)
    {
        try
        {
            Client client = Session.getClient();

            SearchTweetsRequest searchTweetsRequest = new SearchTweetsRequest(keyword, Session.getCurrentUser().getId());

            Request request = new Request(RequestType.SEARCH_TWEETS, searchTweetsRequest);

            client.sendRequest(request);

            Response response = client.getResponse();

            if (response.getType() == ResponseType.SEARCH_TWEETS_SUCCESS)
            {
                List<Tweet> tweets = (List<Tweet>) response.getBody();
                TweetUtil.loadTweets(tweetContainer, tweets);
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public void showHashtag(String hashtag)
    {
        try
        {
            Client client = Session.getClient();

            ShowHashtagRequest showHashtagRequest = new ShowHashtagRequest(hashtag, Session.getCurrentUser().getId());

            Request request = new Request(RequestType.SHOW_HASHTAG, showHashtagRequest);

            client.sendRequest(request);

            Response response = client.getResponse();

            if(response.getType() == ResponseType.SHOW_HASHTAG_SUCCESS)
            {
                List<Tweet> tweets = (List<Tweet>) response.getBody();
                TweetUtil.loadTweets(tweetContainer, tweets);
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
