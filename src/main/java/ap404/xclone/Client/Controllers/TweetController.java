package ap404.xclone.Client.Controllers;

import ap404.xclone.Shared.Models.Tweet;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TweetController
{
    @FXML private Label nameLabel;
    @FXML private Label usernameLabel;
    @FXML private Label contentLabel;
    @FXML private ImageView avatarImage;
    @FXML private Label timeLabel;

    public void setTweet(Tweet tweet)
    {
        nameLabel.setText(tweet.getName());
        usernameLabel.setText(tweet.getUsername());
        contentLabel.setText(tweet.getContent());

        if (tweet.getAvatarImageUrl() != null) avatarImage.setImage(new Image(tweet.getAvatarImageUrl()));
        timeLabel.setText(formatTime(tweet.getCreatedAt()));
    }

    private String formatTime(Timestamp timestamp)
    {
        LocalDateTime tweetTime = timestamp.toLocalDateTime();
        LocalDateTime now = LocalDateTime.now();

        Duration duration = Duration.between(tweetTime, now);

        if (duration.toMinutes() < 1) return "now";
        if (duration.toHours() < 1) return  duration.toMinutes() + "m";
        if (duration.toDays() < 1) return duration.toHours() + "h";
        if (tweetTime.getYear() == now.getYear()) return tweetTime.format(DateTimeFormatter.ofPattern("MMM d"));
        else return tweetTime.format(DateTimeFormatter.ofPattern("MMM d, yyyy"));
    }
}