package ap404.xclone.Client.Controllers;

import ap404.xclone.Client.Client;
import ap404.xclone.Client.Managers.Navigation;
import ap404.xclone.Client.Managers.Session;
import ap404.xclone.Client.Utils.UserUtil;
import ap404.xclone.Shared.DTO.enums.RequestType;
import ap404.xclone.Shared.DTO.enums.ResponseType;
import ap404.xclone.Shared.DTO.request.CreateTweetRequest;
import ap404.xclone.Shared.DTO.request.Request;
import ap404.xclone.Shared.DTO.response.Response;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class PostController
{
    @FXML private ImageView composeAvatar;
    @FXML private TextArea tweetArea;

    @FXML
    public void initialize()
    {
        UserUtil.loadUser(Session.getCurrentUser(),
                null, null,
                null, composeAvatar,
                null, null
        );
    }

    @FXML
    public void post()
    {
        String content = tweetArea.getText();

        if (content == null || content.isBlank()) {
            return;
        }

        try {
            Client client = Session.getClient();

            CreateTweetRequest createTweetRequest = new CreateTweetRequest(
                    Session.getCurrentUser().getId(),
                    content
            );

            Request request = new Request(RequestType.CREATE_TWEET, createTweetRequest);

            client.sendRequest(request);

            Response response = client.getResponse();

            if (response.getType() == ResponseType.CREATE_TWEET_SUCCESS) {
                tweetArea.clear();
                ((Stage) tweetArea.getScene().getWindow()).close();
                if (Navigation.getHomeController() != null) {
                    Navigation.getHomeController().loadTweets();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
