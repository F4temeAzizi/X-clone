package ap404.xclone.Client.Controllers;

import ap404.xclone.Client.Utils.TweetUtil;
import ap404.xclone.Shared.Models.Tweet;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ReplyController {

    @FXML private Label charCount;
    @FXML private Button replyBtn;
    @FXML private TextArea replyArea;
    @FXML private VBox tweetContainer;
    @FXML private Label replyingToLabel;

    private static final int MAX_REPLY_LENGTH = 280;
    private Tweet tweet;
    private String text;

    @FXML
    public void initialize() {

        replyBtn.setDisable(true);
        charCount.setText("0/" + MAX_REPLY_LENGTH);

        replyArea.textProperty().addListener((observable, oldValue, newValue) -> {

            int length = newValue.length();

            charCount.setText(length + "/" + MAX_REPLY_LENGTH);

            replyBtn.setDisable(newValue.isBlank() || length > MAX_REPLY_LENGTH);
        });
    }

    public void setTweet(Tweet tweet) {
        this.tweet = tweet;

        replyingToLabel.setText("Replying to @" + tweet.getUsername());

        try {
            TweetController tweetController = TweetUtil.addTweet(tweetContainer, tweet);
            tweetController.setReadOnly(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void reply() {
        text = replyArea.getText().trim();
        ((Stage) replyArea.getScene().getWindow()).close();
    }

    public String getReplyText() { return text; }
}
