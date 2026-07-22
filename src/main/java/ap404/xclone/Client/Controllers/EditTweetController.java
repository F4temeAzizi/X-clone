package ap404.xclone.Client.Controllers;

import ap404.xclone.Client.Managers.Session;
import ap404.xclone.Client.Utils.MediaUtil;
import ap404.xclone.Client.Utils.UserUtil;
import ap404.xclone.Shared.Models.Media;
import ap404.xclone.Shared.Models.Tweet;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class EditTweetController
{
    @FXML private Label charCount;
    @FXML private Button saveBtn;
    @FXML private TextArea tweetArea;
    @FXML private ImageView composeAvatar;
    @FXML private FlowPane previewPane;
    private List<Media> mediaList = new ArrayList<>();
    private String editedText;
    private final int MAX_TWEET_LENGTH = 280;

    @FXML
    public void initialize() {

        saveBtn.setDisable(true);
        charCount.setText("0/" + MAX_TWEET_LENGTH);

        tweetArea.textProperty().addListener((observable, oldValue, newValue) -> {

            int length = newValue.length();

            charCount.setText(length + "/" + MAX_TWEET_LENGTH);

            saveBtn.setDisable(newValue.isBlank() || length > MAX_TWEET_LENGTH);
        });
    }
    public void setTweet(Tweet tweet)
    {
        UserUtil.loadUser(Session.getCurrentUser(),
                null, null ,
                null,  composeAvatar,
                null, null);
        tweetArea.setText(tweet.getContent());
        mediaList.clear();

        if (tweet.getMedia() != null) mediaList.addAll(tweet.getMedia());
        MediaUtil.showPreview(previewPane, mediaList);
    }

    public String getEditedText() { return editedText; }
    public List<Media> getEditedMedia() { return mediaList; }

    @FXML
    private void save()
    {
        editedText = tweetArea.getText().trim();
        ((Stage) tweetArea.getScene().getWindow()).close();
    }

    @FXML
    private void addPhoto()
    {
        MediaUtil.addPhotos(previewPane, mediaList, tweetArea.getScene().getWindow());
    }

    @FXML
    private void addVideo()
    {
        MediaUtil.addVideos(previewPane, mediaList, tweetArea.getScene().getWindow());
    }
}
