package ap404.xclone.Client.Controllers;

import ap404.xclone.Client.Managers.Navigation;
import ap404.xclone.Client.Utils.MediaUtil;
import ap404.xclone.Client.Utils.TweetUtil;
import ap404.xclone.Client.Utils.UserUtil;
import ap404.xclone.Shared.DTO.enums.PageType;
import ap404.xclone.Shared.Models.Media;
import ap404.xclone.Shared.Models.Tweet;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import ap404.xclone.Client.Client;
import ap404.xclone.Client.Managers.Session;
import ap404.xclone.Shared.DTO.enums.RequestType;
import ap404.xclone.Shared.DTO.enums.ResponseType;
import ap404.xclone.Shared.DTO.request.CreateTweetRequest;
import ap404.xclone.Shared.DTO.request.Request;
import ap404.xclone.Shared.DTO.response.Response;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.List;

public class HomeController
{
    @FXML private Label charCount;
    @FXML private Button postBtn;
    @FXML private VBox tweetContainer;
    @FXML private ImageView composeAvatar;
    @FXML private TextArea tweetTextArea;
    @FXML private ScrollPane scrollPane;
    @FXML private FlowPane previewPane;
    private List<Media> mediaList = new ArrayList<>();
    private final int MAX_TWEET_LENGTH = 280;

    @FXML
    public void initialize() {

        Navigation.setPreviousPage(PageType.HOME);
        Navigation.setProfileReturnPage(PageType.HOME);
        Navigation.clearHistory();
        postBtn.setDisable(true);
        charCount.setText("0/" + MAX_TWEET_LENGTH);

        tweetTextArea.textProperty().addListener((observable, oldValue, newValue) -> {

            int length = newValue.length();

            charCount.setText(length + "/" + MAX_TWEET_LENGTH);

            updatePostButton();
        });

        tweetTextArea.setPrefHeight(60);
        tweetTextArea.textProperty().addListener((obs, o, n) -> {

            Text text = new Text(n);
            text.setFont(tweetTextArea.getFont());

            text.setWrappingWidth(tweetTextArea.getWidth() - 25);

            double h = text.getLayoutBounds().getHeight();

            tweetTextArea.setPrefHeight(Math.max(60, h + 30));
        });

        tweetTextArea.widthProperty().addListener((obs, o, n) -> {
            tweetTextArea.setText(tweetTextArea.getText());
        });

        Navigation.setHomeController(this);
        tweetTextArea.setText(Navigation.getComposeText());
        tweetTextArea.textProperty().addListener((obs, o, n) ->
                Navigation.setComposeText(n));

        updateComposeAvatar();
        loadTweets();

        scrollPane.layout();
        scrollPane.setVvalue(Navigation.getHomeScroll());
        scrollPane.vvalueProperty().addListener((obs, o, n) ->
                Navigation.setHomeScroll(n.doubleValue()));
    }

    private void updatePostButton()
    {
        boolean hasText = !tweetTextArea.getText().trim().isBlank();
        boolean hasMedia = !mediaList.isEmpty();

        postBtn.setDisable((!hasText && !hasMedia) || tweetTextArea.getText().length() > MAX_TWEET_LENGTH);
    }

    @FXML
    private void postTweet() {
        String content = tweetTextArea.getText();

        boolean hasText = content != null && !content.isBlank();
        boolean hasMedia = !mediaList.isEmpty();

        if (!hasText && !hasMedia) return;

        try {
            Client client = Session.getClient();

            CreateTweetRequest createTweetRequest = new CreateTweetRequest(
                    Session.getCurrentUser().getId(),
                    content,
                    new ArrayList<>(mediaList)
            );

            Request request = new Request(RequestType.CREATE_TWEET, createTweetRequest);

            client.sendRequest(request);

            Response response = client.getResponse();

            if (response.getType() == ResponseType.CREATE_TWEET_SUCCESS) {
                tweetTextArea.clear();
                mediaList = new ArrayList<>();
                MediaUtil.showPreview(previewPane, mediaList);
                updatePostButton();
                Navigation.setComposeText("");
                loadTweets();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void loadTweets() {
        try {
            Client client = Session.getClient();

            Request request = new Request(RequestType.GET_FEED, Session.getCurrentUser().getId());

            client.sendRequest(request);

            Response response = client.getResponse();

            if (response.getType() == ResponseType.GET_TWEETS_SUCCESS) {

                List<Tweet> tweets = (List<Tweet>) response.getBody();
                TweetUtil.loadTweets(tweetContainer, tweets);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateComposeAvatar ()
    {
        UserUtil.loadUser(Session.getCurrentUser(),
                null, null ,
                null,  composeAvatar,
                null, null);
    }

    @FXML
    public void addPhoto()
    {
        MediaUtil.addPhotos(previewPane, mediaList, tweetTextArea.getScene().getWindow());
        updatePostButton();
    }

    @FXML
    public void addVideo()
    {
        MediaUtil.addVideos(previewPane, mediaList, tweetTextArea.getScene().getWindow());
        updatePostButton();
    }

    public VBox getTweetContainer() { return tweetContainer; }
}
