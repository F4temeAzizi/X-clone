package ap404.xclone.Client.Controllers;

import ap404.xclone.Shared.Models.Tweet;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TweetController
{
    @FXML private Label nameLabel;
    @FXML private Label usernameLabel;
    @FXML private Label contentLabel;
    @FXML private ImageView avatarImage;

    public void setTweet(Tweet tweet)
    {
        nameLabel.setText(tweet.getName());
        usernameLabel.setText(tweet.getUsername());
        contentLabel.setText(tweet.getContent());

        if (tweet.getAvatarImageUrl() != null) avatarImage.setImage(new Image(tweet.getAvatarImageUrl()));
    }
}