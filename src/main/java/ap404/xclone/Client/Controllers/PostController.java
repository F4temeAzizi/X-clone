package ap404.xclone.Client.Controllers;

import ap404.xclone.Client.Client;
import ap404.xclone.Client.Managers.Navigation;
import ap404.xclone.Client.Managers.Session;
import ap404.xclone.Client.Utils.MediaUtil;
import ap404.xclone.Client.Utils.UserUtil;
import ap404.xclone.Shared.DTO.enums.RequestType;
import ap404.xclone.Shared.DTO.enums.ResponseType;
import ap404.xclone.Shared.DTO.request.CreateTweetRequest;
import ap404.xclone.Shared.DTO.request.Request;
import ap404.xclone.Shared.DTO.response.Response;
import ap404.xclone.Shared.Models.Media;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PostController
{
    @FXML private ImageView composeAvatar;
    @FXML private TextArea tweetArea;
    @FXML private FlowPane previewPane;

    private List<Media> mediaList = new ArrayList<>();

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
                tweetArea.clear();
                mediaList = new ArrayList<>();
                MediaUtil.showPreview(previewPane, mediaList);
                Navigation.setComposeText("");
                ((Stage) tweetArea.getScene().getWindow()).close();
                if (Navigation.getHomeController() != null) {
                    Navigation.getHomeController().loadTweets();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void addPhoto()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images","*.png","*.jpg","*.jpeg"));

        List<File> files = fileChooser.showOpenMultipleDialog(tweetArea.getScene().getWindow());

        if (files == null) return;

        for (File file : files)
        {
            if (mediaList.size() == 4) break;
            mediaList.add(new Media(
                    file.getAbsolutePath(),
                    "image",
                    mediaList.size()
            ));
            MediaUtil.showPreview(previewPane, mediaList);
        }
        System.out.println(mediaList.size());
    }
}
