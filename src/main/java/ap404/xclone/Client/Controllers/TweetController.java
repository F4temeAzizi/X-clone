package ap404.xclone.Client.Controllers;

import ap404.xclone.Client.Client;
import ap404.xclone.Client.Managers.Session;
import ap404.xclone.Client.Managers.ThemeManager;
import ap404.xclone.Shared.DTO.enums.RequestType;
import ap404.xclone.Shared.DTO.enums.ResponseType;
import ap404.xclone.Shared.DTO.request.*;
import ap404.xclone.Client.Managers.Navigation;
import ap404.xclone.Shared.DTO.response.Response;
import ap404.xclone.Shared.Models.Tweet;
import ap404.xclone.Shared.Models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;


public class TweetController
{
    @FXML private Label retweetCountLabel;
    @FXML private Button retweetBtn;
    @FXML private Label retweetedByLabel;
    @FXML private HBox retweetHeader;
    @FXML private Label likeCountLabel;
    @FXML private Button likeBtn;
    @FXML private Button bookmarkBtn;
    @FXML private Button moreBtn;
    @FXML private Label nameLabel;
    @FXML private Label usernameLabel;
    @FXML private Label contentLabel;
    @FXML private ImageView avatarImage;
    @FXML private Label timeLabel;
    @FXML private HBox tweetRoot;

    private Tweet tweet;
    private Tweet target;

    public void setTweet(Tweet tweet)
    {
        this.tweet = tweet;

        target = getRootTweet(tweet);

        nameLabel.setText(target.getName());
        usernameLabel.setText(target.getUsername());
        contentLabel.setText(target.getContent());

        if (target.getAvatarImageUrl() != null)
            avatarImage.setImage(new Image(target.getAvatarImageUrl()));


        boolean isCurrentUser = (Session.getCurrentUser().getId() == tweet.getUserId());
        moreBtn.setVisible(isCurrentUser);
        moreBtn.setManaged(isCurrentUser);

        if(tweet.isRetweet()){
            retweetHeader.setVisible(true);
            retweetHeader.setManaged(true);

            if(isCurrentUser) {
                retweetedByLabel.setText("You Retweeted");
            }
            else {
                retweetedByLabel.setText(tweet.getUsername() + " Retweeted");
            }
        }

        updateRetweetUI();
        updateLikeUI();
        updateBookmarkUI();
        timeLabel.setText(formatTime(tweet.getCreatedAt()));
    }

    @FXML
    public void showTweetMenu()
    {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem delete = new MenuItem("Delete");
        MenuItem edit = new MenuItem("Edit");
        contextMenu.getItems().addAll(delete, edit);

        contextMenu.getStyleClass().add("x-menu");
        contextMenu.show(moreBtn, javafx.geometry.Side.TOP, 0, 0);

        delete.setOnAction(event -> deleteTweet());
        edit.setOnAction(e -> editTweet());
    }

    public void deleteTweet()
    {
        try
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

            alert.setTitle("Delete tweet");
            alert.setHeaderText("Are you sure you want to delete this tweet?");

            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(ThemeManager.getThemeCss());
            dialogPane.getStylesheets().add(getClass().getResource("/css/alert.css").toExternalForm());
            dialogPane.getStyleClass().add("alert");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isEmpty() || result.get() != ButtonType.OK) return;

            Client client = new Client();

            DeleteTweetRequest deleteTweetRequest = new DeleteTweetRequest(tweet.getId(), tweet.getUserId());

            client.sendRequest(new Request(RequestType.DELETE_TWEET, deleteTweetRequest));
            Response response = client.getResponse();

            if (response.getType() == ResponseType.DELETE_TWEET_SUCCESS)
            {
                ((VBox) tweetRoot.getParent()).getChildren().remove(tweetRoot);
            }
        }
        catch (IOException | ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void editTweet()
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edit-tweet.fxml"));
        Parent root;

        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        EditTweetController controller = loader.getController();
        controller.setTweet(tweet);

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        ThemeManager.applyTheme(scene);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.showAndWait();

        String newContent = controller.getEditedText();

        if (newContent == null) return;
        if (newContent.isBlank() || newContent.equals(tweet.getContent())) return;

        try
        {
            Client client = new Client();

            EditTweetRequest editTweetRequest = new EditTweetRequest(tweet.getId(), tweet.getUserId(), newContent);

            client.sendRequest(new Request(RequestType.EDIT_TWEET, editTweetRequest));

            Response response = client.getResponse();

            if (response.getType() == ResponseType.EDIT_TWEET_SUCCESS)
            {
                tweet.setContent(newContent);
                contentLabel.setText(newContent);
            }
        }
        catch (IOException | ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void handleLike() throws IOException, ClassNotFoundException {

        boolean liked = target.isLikedByUser();
        Client client = Session.getClient();

        if(liked) {
            Request request = new Request(
                    RequestType.UNLIKE,
                    new LikeRequest(Session.getCurrentUser().getId(), target.getId())
            );

            client.sendRequest(request);

            ResponseType responseType = client.getResponse().getType();

            if(responseType == ResponseType.UNLIKE_SUCCESS) {
                target.setLikeCount(target.getLikeCount() - 1);
                target.setLikedByUser(false);
                updateLikeUI();
            }
        }
        else {
            Request request = new Request(
                    RequestType.LIKE,
                    new LikeRequest(Session.getCurrentUser().getId(), target.getId())
            );

            client.sendRequest(request);

            ResponseType responseType = client.getResponse().getType();

            if(responseType == ResponseType.LIKE_SUCCESS) {
                target.setLikeCount(target.getLikeCount() + 1);
                target.setLikedByUser(true);
                updateLikeUI();
            }
        }
    }

    private void updateLikeUI() {

        likeCountLabel.setText(String.valueOf(target.getLikeCount()));

        if (target.isLikedByUser()) {
            if (!likeBtn.getStyleClass().contains("liked")) {
                likeBtn.getStyleClass().add("liked");
            }
         } else
            likeBtn.getStyleClass().remove("liked");

    }

    public void handleBookmark() throws IOException, ClassNotFoundException
    {
        boolean bookmarked = target.isBookmarkedByUser();
        Client client = Session.getClient();

        if (bookmarked)
        {
            Request request = new Request(RequestType.UNBOOKMARK,
                    new BookmarkRequest(Session.getCurrentUser().getId(), target.getId()));

            client.sendRequest(request);
            ResponseType responseType = client.getResponse().getType();

            if (responseType == ResponseType.UNBOOKMARK_SUCCESS)
            {
                target.setBookmarkedByUser(false);
                updateBookmarkUI();
            }
        }
        else
        {
            Request request = new Request(RequestType.BOOKMARK,
                    new BookmarkRequest(Session.getCurrentUser().getId(), target.getId()));

            client.sendRequest(request);
            ResponseType responseType = client.getResponse().getType();

            if (responseType == ResponseType.BOOKMARK_SUCCESS)
            {
                target.setBookmarkedByUser(true);
                updateBookmarkUI();
            }
        }
    }

    private void updateBookmarkUI()
    {

      if (target.isBookmarkedByUser())
      {
          if (!bookmarkBtn.getStyleClass().contains("bookmarked"))
          {
              bookmarkBtn.getStyleClass().add("bookmarked");
          }
      }
      else bookmarkBtn.getStyleClass().remove("bookmarked");
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

        if (target.getUserId() == Session.getCurrentUser().getId()) Navigation.loadProfile();
        else
        {
            try
            {
                Client client = new Client();
                GetUserByIdRequest getUserByIdRequest = new GetUserByIdRequest(target.getUserId());
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

    public void handleRetweet() throws IOException, ClassNotFoundException {

        boolean isRetweetedByUser = target.isRetweetedByUser();
        Client client = Session.getClient();

        if(!isRetweetedByUser) {

            RetweetRequest retweetRequest = new RetweetRequest(
                    Session.getCurrentUser().getId(),
                    target.getId()
            );

            client.sendRequest(new Request(RequestType.RETWEET, retweetRequest));

            Response response = client.getResponse();

            if(response.getType() == ResponseType.RETWEET_SUCCESS) {
                target.setRetweetCount(target.getRetweetCount()+1);
                tweet.setOriginalTweet(target);
                target.setRetweetedByUser(true);

                updateRetweetUI();
            }
        }
    }

    public void updateRetweetUI() {

        retweetCountLabel.setText(String.valueOf(target.getRetweetCount()));

        if(target.isRetweetedByUser()) {
            if(!retweetBtn.getStyleClass().contains("retweeted-active")){
                retweetBtn.getStyleClass().add("retweeted-active");
            }
        }
        else
            retweetBtn.getStyleClass().remove("retweeted-active");
    }

    public Tweet getRootTweet(Tweet tweet) {

        while (tweet.isRetweet() && tweet.getOriginalTweet() != null) {
            tweet = tweet.getOriginalTweet();
        }

        return tweet;
    }
}