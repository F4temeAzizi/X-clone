package ap404.xclone.Client.Controllers;

import ap404.xclone.Client.Client;
import ap404.xclone.Client.Managers.Navigation;
import ap404.xclone.Client.Managers.Session;
import ap404.xclone.Shared.DTO.enums.RequestType;
import ap404.xclone.Shared.DTO.enums.ResponseType;
import ap404.xclone.Shared.DTO.request.Request;
import ap404.xclone.Shared.DTO.request.UpdateProfileRequest;
import ap404.xclone.Shared.DTO.response.Response;
import ap404.xclone.Shared.Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;

import java.io.File;

public class EditProfileController
{
    @FXML private TextField nameField;
    @FXML private TextField usernameField;
    @FXML private TextArea bioField;
    @FXML private ImageView avatarImage;
    @FXML private Region bannerRegion;

    private String avatarImagePath;
    private String bannerImagePath;

    public  void initialize()
    {
        User user = Session.getCurrentUser();

        nameField.setText(user.getDisplayName());
        usernameField.setText(user.getUsername());
        if (user.getBio() != null) {
            bioField.setText(user.getBio());
        }

        avatarImagePath = user.getProfileImageUrl();
        bannerImagePath = user.getBannerImageUrl();

        if (avatarImagePath != null) {
            avatarImage.setImage(new Image(avatarImagePath));
        }

        if (bannerImagePath != null) {
            bannerRegion.setStyle("-fx-background-image: url('" + bannerImagePath + "');");
        }
    }

    @FXML
    public void changeBanner()
    {
        FileChooser fileChooser =  new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images","*.png","*.jpg","*jpeg"));

        File file = fileChooser.showOpenDialog(null);

        if (file == null) return;

        bannerImagePath = file.toURI().toString();
        bannerRegion.setStyle("-fx-background-image: url('" + bannerImagePath + "');");
    }

    @FXML
    public void changeProfilePicture()
    {
        FileChooser fileChooser =  new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images","*.png","*.jpg","*jpeg"));

        File file = fileChooser.showOpenDialog(null);

        if (file == null) return;

        avatarImagePath = file.toURI().toString();
        avatarImage.setImage(new Image(avatarImagePath));
    }

    @FXML
    public void save ()
    {
        try {
            Client client = new Client();

            User user = Session.getCurrentUser();

            UpdateProfileRequest request = new UpdateProfileRequest(
                    user.getId(),
                    nameField.getText(),
                    usernameField.getText(),
                    bioField.getText(),
                    bannerImagePath,
                    avatarImagePath
            );

            client.sendRequest(new Request(RequestType.UPDATE_PROFILE, request));
            Response response = client.getResponse();

            if (response.getType().equals(ResponseType.UPDATE_PROFILE_SUCCESS))
            {
                Session.setCurrentUser((User) response.getBody());
                Navigation.getMainController().updateUserProfile();
                if (Navigation.getHomeController() != null) {
                    Navigation.getHomeController().updateComposeAvatar();
                }
                Navigation.loadProfile();
            }
            else {
                System.out.println("Failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML public void cancel () { Navigation.loadProfile(); }
}
