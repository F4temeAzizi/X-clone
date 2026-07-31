package ap404.xclone.Client.Controllers;

import ap404.xclone.Client.Client;
import ap404.xclone.Client.Managers.Navigation;
import ap404.xclone.Client.Managers.Session;
import ap404.xclone.Client.Utils.TweetUtil;
import ap404.xclone.Shared.DTO.enums.PageType;
import ap404.xclone.Shared.DTO.enums.RequestType;
import ap404.xclone.Shared.DTO.request.GetTweetRepliesRequest;
import ap404.xclone.Shared.DTO.request.Request;
import ap404.xclone.Shared.DTO.response.Response;
import ap404.xclone.Shared.Models.Tweet;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class ShowRepliesController {

    @FXML private Button backButton;
    @FXML private StackPane mainTweetContainer;
    @FXML private VBox repliesContainer;

    @FXML
    public void initialize() {

        try {
            TweetController tweetController = TweetUtil.addTweet(
                    mainTweetContainer,
                    Navigation.getSelectedTweet(),
                    -1
            );
            tweetController.setReadOnly(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        loadReplies();
    }

    public void loadReplies() {

        try {
            Client client = Session.getClient();

            GetTweetRepliesRequest getTweetRepliesRequest = new GetTweetRepliesRequest(
                    Navigation.getSelectedTweet().getId(),
                    Session.getCurrentUser().getId()
            );

            client.sendRequest(new Request(RequestType.GET_TWEET_REPLIES, getTweetRepliesRequest));
            Response response = client.getResponse();

            List<Tweet> replies = (List<Tweet>) response.getBody();

            TweetUtil.loadTweets(repliesContainer, replies);
        }
        catch (Exception e) {
            System.out.println("error getting replies: " + e.getMessage());
        }
    }

    public void handleBack() {

        Navigation.removeLastReply();
        Tweet tweet = Navigation.getCurrentReply();

        if(tweet == null) {
            PageType pageType = Navigation.getPreviousPage();

            switch (pageType) {

                case HOME :
                    Navigation.loadHome();
                    return;
                case EXPLORE :
                    Navigation.loadExplore();
                    return;
                case PROFILE :
                    Navigation.loadProfile();
                    return;
                case OTHER_PROFILE :
                    Navigation.loadOthersProfile();
                    return;
                case BOOKMARK :
                    Navigation.loadBookmark();
                    return;
            }
        }

        Navigation.setSelectedTweet(tweet);
        Navigation.loadShowReplies();
    }
}
