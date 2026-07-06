package ap404.xclone.Client.Controllers;

import ap404.xclone.Client.Managers.Session;
import ap404.xclone.Shared.Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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

    public  void initialize()
    {
        User user = Session.getCurrentUser();

        nameField.setText(user.getDisplayName());
        usernameField.setText(user.getUsername());
        if (user.getBio() != null) {
            bioField.setText(user.getBio());
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

        bannerRegion.setStyle("-fx-background-image: url('" + file.toURI().toString() + "');");
    }

    @FXML
    public void changeProfilePicture()
    {
    
    }
}
