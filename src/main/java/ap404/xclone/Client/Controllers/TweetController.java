package ap404.xclone.Client.Controllers;

import ap404.xclone.Client.Client;
import ap404.xclone.Client.Managers.Session;
import ap404.xclone.Shared.DTO.enums.RequestType;
import ap404.xclone.Shared.DTO.enums.ResponseType;
import ap404.xclone.Shared.DTO.request.LikeRequest;
import ap404.xclone.Shared.DTO.request.Request;
import ap404.xclone.Client.Managers.Navigation;
import ap404.xclone.Shared.DTO.request.GetUserByIdRequest;
import ap404.xclone.Shared.DTO.response.Response;
import ap404.xclone.Shared.Models.Tweet;
import ap404.xclone.Shared.Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class TweetController
{
    @FXML private Label likeCountLabel;
    @FXML private Button likeBtn;
    @FXML private Button moreBtn;
    @FXML private Label nameLabel;
    @FXML private Label usernameLabel;
    @FXML private Label contentLabel;
    private Tweet tweet;

    @FXML private ImageView avatarImage;
    @FXML private Label timeLabel;

    public void setTweet(Tweet tweet)
    {
        this.tweet = tweet;
        nameLabel.setText(tweet.getName());
        usernameLabel.setText(tweet.getUsername());
        contentLabel.setText(tweet.getContent());

        boolean isCurrentUser = (Session.getCurrentUser().getId() == tweet.getUserId());
        moreBtn.setVisible(isCurrentUser);
        moreBtn.setManaged(isCurrentUser);

        updateLikeUI();
    }

    @FXML
    public void showTweetMenu()
    {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem delete = new MenuItem("Delete");
        MenuItem edit = new MenuItem("Edit");
        contextMenu.getItems().addAll(delete, edit);

        contextMenu.getStyleClass().add("x-menu");
        contextMenu.show(moreBtn, javafx.geometry.Side.BOTTOM, 0, 0);
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
        } else
            likeBtn.getStyleClass().remove("liked");

        if (tweet.getAvatarImageUrl() != null)
            avatarImage.setImage(new Image(tweet.getAvatarImageUrl()));
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

    @FXML
    private void showProfile() {
        if (tweet.getUserId() == Session.getCurrentUser().getId()) Navigation.loadProfile();
        else
        {
            try
            {
                Client client = new Client();
                GetUserByIdRequest getUserByIdRequest = new GetUserByIdRequest(tweet.getUserId());
                client.sendRequest(new Request(RequestType.GET_USER_BY_ID, getUserByIdRequest));

                Response response = client.getResponse();

                if (response.getType() == ResponseType.GET_USER_BY_ID_SUCCESS)
                {
                    User user = (User) response.getBody();
                    Navigation.setSelectedUser(user);
                    Navigation.loadOthersProfile();
                }
                else System.out.println("user not found");

            }
            catch (IOException | ClassNotFoundException e)
            {
                throw new RuntimeException(e);
            }
        }
    }
}