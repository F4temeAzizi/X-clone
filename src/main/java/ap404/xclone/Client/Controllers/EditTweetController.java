package ap404.xclone.Client.Controllers;

import ap404.xclone.Client.Managers.Session;
import ap404.xclone.Client.Utils.UserUtil;
import ap404.xclone.Shared.Models.Tweet;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class EditTweetController
{
    @FXML private TextArea tweetArea;
    @FXML private ImageView composeAvatar;
    private String editedText;

    public void setTweet(Tweet tweet)
    {
        UserUtil.loadUser(Session.getCurrentUser(),
                null, null ,
                null,  composeAvatar,
                null, null);
        tweetArea.setText(tweet.getContent());
    }

    public String getEditedText() { return editedText; }

    @FXML
    private void save()
    {
        editedText = tweetArea.getText().trim();
        ((Stage) tweetArea.getScene().getWindow()).close();
    }
}
