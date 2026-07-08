package ap404.xclone.Client.Controllers;

import ap404.xclone.Client.Client;
import ap404.xclone.Client.Managers.Session;
import ap404.xclone.Shared.DTO.enums.RequestType;
import ap404.xclone.Shared.DTO.enums.ResponseType;
import ap404.xclone.Shared.DTO.request.LikeRequest;
import ap404.xclone.Shared.DTO.request.Request;
import ap404.xclone.Shared.DTO.response.Response;
import ap404.xclone.Shared.Models.Tweet;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.lang.reflect.Type;

public class TweetController
{
    @FXML private Button likeBtn;
    @FXML private Label nameLabel;
    @FXML private Label usernameLabel;
    @FXML private Label contentLabel;
    private int tweetId;

    public void setTweet(Tweet tweet)
    {
        tweetId = tweet.getId();
        nameLabel.setText(tweet.getName());
        usernameLabel.setText(tweet.getUsername());
        contentLabel.setText(tweet.getContent());
    }

    public void like() throws IOException, ClassNotFoundException {

        boolean liked = likeBtn.getStyleClass().contains("liked");
        Client client = Session.getClient();

        if(liked) {
            Request request = new Request(
                    RequestType.UNLIKE,
                    new LikeRequest(Session.getCurrentUser().getId(), tweetId)
            );

            client.sendRequest(request);

            ResponseType responseType = client.getResponse().getType();

            if(responseType == ResponseType.UNLIKE_SUCCESS) {
                likeBtn.getStyleClass().remove("liked");
            }
        }
        else {
            Request request = new Request(
                    RequestType.LIKE,
                    new LikeRequest(Session.getCurrentUser().getId(), tweetId)
            );

            client.sendRequest(request);

            ResponseType responseType = client.getResponse().getType();

            if(responseType == ResponseType.LIKE_SUCCESS)
                likeBtn.getStyleClass().add("liked");
        }
    }
}