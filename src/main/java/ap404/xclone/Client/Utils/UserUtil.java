package ap404.xclone.Client.Utils;

import ap404.xclone.Client.Managers.Session;
import ap404.xclone.Shared.Models.User;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;

public class UserUtil
{
    public static void loadUser (Label name, Label username, Label bio, ImageView avatar, Region banner)
    {
        User user = Session.getCurrentUser();
        if (name != null ) name.setText(user.getDisplayName());
        if (username != null ) username.setText("@" + user.getUsername());

        if (user.getBio() != null && bio != null) {
            bio.setText(user.getBio());
        }

        if (user.getProfileImageUrl() != null && avatar != null) {
            avatar.setImage(new Image(user.getProfileImageUrl()));
        }

        if (user.getBannerImageUrl() != null && banner != null) {
            banner.setStyle("-fx-background-image: url('" + user.getBannerImageUrl() + "');");
        }
    }
}
