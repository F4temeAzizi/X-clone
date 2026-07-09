package ap404.xclone.Client.Controllers;

import ap404.xclone.Client.Managers.Navigation;
import ap404.xclone.Client.Managers.Session;
import ap404.xclone.Client.Utils.UserUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class ProfileController
{
    @FXML private Label nameLbl;
    @FXML private Label usernameLbl;
    @FXML private Label bioLbl;
    @FXML private ImageView avatarImage;
    @FXML private Region bannerRegion;
    @FXML private Label createdAtLbl;
    @FXML private Label postsTab;
    @FXML private Label repliesTab;
    @FXML private Label mediaTab;
    @FXML private Label likesTab;
    @FXML private ScrollPane postsPane;
    @FXML private VBox emptyPane;


    public void initialize ()
    {
        UserUtil.loadUser(Session.getCurrentUser(), nameLbl, usernameLbl, bioLbl, avatarImage, bannerRegion, createdAtLbl);
    }

    @FXML public void goToEditProfile() { Navigation.loadEditProfile(); }

    @FXML public void showPosts ()
    {
        selectTab(postsTab);

        postsPane.setVisible(true);
        postsPane.setManaged(true);

        emptyPane.setVisible(false);
        emptyPane.setManaged(false);
    }

    @FXML public void showReplies ()
    {

        selectTab(repliesTab);

        postsPane.setVisible(false);
        postsPane.setManaged(false);

        emptyPane.setVisible(true);
        emptyPane.setManaged(true);
    }

    @FXML public void showMedia ()
    {
        selectTab(mediaTab);

        postsPane.setVisible(false);
        postsPane.setManaged(false);

        emptyPane.setVisible(true);
        emptyPane.setManaged(true);
    }

    @FXML public void showLikes ()
    {
        selectTab(likesTab);

        postsPane.setVisible(false);
        postsPane.setManaged(false);

        emptyPane.setVisible(true);
        emptyPane.setManaged(true);
    }

    private void selectTab(Label active)
    {
        postsTab.getStyleClass().setAll("profile-tab");
        repliesTab.getStyleClass().setAll("profile-tab");
        mediaTab.getStyleClass().setAll("profile-tab");
        likesTab.getStyleClass().setAll("profile-tab");

        active.getStyleClass().setAll("profile-tab-active");
    }
}

