package ap404.xclone.Client.Controllers;

import ap404.xclone.Client.Client;
import ap404.xclone.Client.Managers.Session;
import ap404.xclone.Shared.DTO.enums.RequestType;
import ap404.xclone.Shared.DTO.enums.ResponseType;
import ap404.xclone.Shared.DTO.request.LikeRequest;
import ap404.xclone.Shared.DTO.request.Request;
import ap404.xclone.Shared.Models.Tweet;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class TweetController
{
    @FXML private Label likeCountLabel;
    @FXML private Button likeBtn;
    @FXML private Label nameLabel;
    @FXML private Label usernameLabel;
    @FXML private Label contentLabel;
    private Tweet tweet;


    public void setTweet(Tweet tweet)
    {
        this.tweet = tweet;
        nameLabel.setText(tweet.getName());
        usernameLabel.setText(tweet.getUsername());
        contentLabel.setText(tweet.getContent());

        updateLikeUI();
    }

    public void handleLike() throws IOException, ClassNotFoundException {

        boolean liked = tweet.isLikedByUser();
        Client client = Session.getClient();

        if(liked) {
            Request request = new Request(
                    RequestType.UNLIKE,
                    new LikeRequest(Session.getCurrentUser().getId(), tweet.getId())
            );

            client.sendRequest(request);

            ResponseType responseType = client.getResponse().getType();

            if(responseType == ResponseType.UNLIKE_SUCCESS) {
                tweet.setLikeCount(tweet.getLikeCount() - 1);
                tweet.setLikedByUser(false);
                updateLikeUI();
            }
        }
        else {
            Request request = new Request(
                    RequestType.LIKE,
                    new LikeRequest(Session.getCurrentUser().getId(), tweet.getId())
            );

            client.sendRequest(request);

            ResponseType responseType = client.getResponse().getType();

            if(responseType == ResponseType.LIKE_SUCCESS) {
                tweet.setLikeCount(tweet.getLikeCount() + 1);
                tweet.setLikedByUser(true);
                updateLikeUI();
            }
        }
    }

    private void updateLikeUI() {
        likeCountLabel.setText(String.valueOf(tweet.getLikeCount()));

        if (tweet.isLikedByUser()) {
            if (!likeBtn.getStyleClass().contains("liked")) {
                likeBtn.getStyleClass().add("liked");
            }
        } else {
            likeBtn.getStyleClass().remove("liked");
        }
    }
}