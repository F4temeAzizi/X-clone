package ap404.xclone.Client.Controllers;

import ap404.xclone.Client.Managers.Navigation;
import ap404.xclone.Client.Utils.UserUtil;
import ap404.xclone.Shared.Models.Tweet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ap404.xclone.Client.Client;
import ap404.xclone.Client.Managers.Session;
import ap404.xclone.Shared.DTO.enums.RequestType;
import ap404.xclone.Shared.DTO.enums.ResponseType;
import ap404.xclone.Shared.DTO.request.CreateTweetRequest;
import ap404.xclone.Shared.DTO.request.Request;
import ap404.xclone.Shared.DTO.response.Response;
import javafx.scene.control.TextArea;

import java.util.List;

public class HomeController
{
    @FXML private VBox tweetContainer;
    @FXML private ImageView composeAvatar;
    @FXML private TextArea tweetTextArea;

    @FXML
    public void initialize() {
        updateComposeAvatar();
        loadTweets();
    }

    @FXML
    private void postTweet() {
        String content = tweetTextArea.getText();

        if (content == null || content.isBlank()) {
            return;
        }

        try {
            Client client = new Client();

            CreateTweetRequest createTweetRequest = new CreateTweetRequest(
                    Session.getCurrentUser().getId(),
                    content
            );

            Request request = new Request(RequestType.CREATE_TWEET, createTweetRequest);

            client.sendRequest(request);

            Response response = client.getResponse();

            if (response.getType() == ResponseType.CREATE_TWEET_SUCCESS) {
                tweetTextArea.clear();
                loadTweets();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadTweets() {
        try {
            Client client = new Client();

            Request request = new Request(RequestType.GET_ALL_TWEETS, null);

            client.sendRequest(request);

            Response response = client.getResponse();

            if (response.getType() == ResponseType.GET_TWEETS_SUCCESS) {
                tweetContainer.getChildren().clear();

                List<Tweet> tweets = (List<Tweet>) response.getBody();

                for (Tweet tweet : tweets) {
                    addTweet(tweet);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addTweet(Tweet tweet) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/tweet.fxml"));

        HBox root = loader.load();

        TweetController controller = loader.getController();
        controller.setTweet(tweet);

        tweetContainer.getChildren().add(root);
    }

    public void updateComposeAvatar ()
    {
        UserUtil.loadUser(null, null ,null,  composeAvatar, null, null);
    }
}
