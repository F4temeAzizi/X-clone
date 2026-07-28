package ap404.xclone.Client.Controllers;

import ap404.xclone.Client.Client;
import ap404.xclone.Client.Managers.Navigation;
import ap404.xclone.Client.Managers.Session;
import ap404.xclone.Client.Utils.FollowUtil;
import ap404.xclone.Client.Utils.UserUtil;
import ap404.xclone.Shared.DTO.enums.RequestType;
import ap404.xclone.Shared.DTO.enums.ResponseType;
import ap404.xclone.Shared.DTO.request.GetFollowersRequest;
import ap404.xclone.Shared.DTO.request.GetFollowingRequest;
import ap404.xclone.Shared.DTO.request.Request;
import ap404.xclone.Shared.DTO.response.Response;
import ap404.xclone.Shared.Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.util.List;

public class FollowListController
{
    @FXML private Label titleLabel;

    @FXML private VBox usersContainer;

    public void initialize()
    {
        String listType = Navigation.getFollowListType();

        if ("followers".equals(listType))
        {
            titleLabel.setText("Followers");
            loadFollowers();
        }
        else
        {
            titleLabel.setText("Following");
            loadFollowing();
        }
    }

    private void loadFollowers()
    {
        try
        {
            Client client = Session.getClient();

            GetFollowersRequest requestBody =
                    new GetFollowersRequest(Navigation.getFollowListUser().getId());

            client.sendRequest(
                    new Request(RequestType.GET_FOLLOWERS, requestBody)
            );

            Response response = client.getResponse();

            if (response.getType() == ResponseType.GET_FOLLOWERS_SUCCESS)
            {
                List<User> users = (List<User>) response.getBody();

                showUsers(users);
            }
        }
        catch (Exception e)
        {
            System.err.println("Failed to load followers: " + e.getMessage());
        }
    }

    private void loadFollowing()
    {
        try
        {
            Client client = Session.getClient();

            GetFollowingRequest requestBody =
                    new GetFollowingRequest(
                            Navigation.getFollowListUser().getId()
                    );

            client.sendRequest(
                    new Request(
                            RequestType.GET_FOLLOWING,
                            requestBody
                    )
            );

            Response response = client.getResponse();

            if (response.getType() == ResponseType.GET_FOLLOWING_SUCCESS)
            {
                List<User> users = (List<User>) response.getBody();

                showUsers(users);
            }
        }
        catch (Exception e)
        {
            System.err.println("Failed to load following: " + e.getMessage());
        }
    }

    private void showUsers(List<User> users)
    {
        usersContainer.getChildren().clear();

        if (users == null || users.isEmpty())
        {
            usersContainer.getChildren().add(
                    new Label("No users found")
            );

            return;
        }

        for (User user : users)
        {
            usersContainer.getChildren().add(
                    createUserRow(user)
            );
        }
    }

    private HBox createUserRow(User user)
    {
        ImageView avatar = new ImageView();
        avatar.setFitWidth(60);
        avatar.setFitHeight(60);

        avatar.setImage(new Image(getClass().getResource("/images/avatar.jpeg").toExternalForm()));

        Circle clip = new Circle(30,30,30);
        avatar.setClip(clip);

        UserUtil.loadUser(user, null, null, null, avatar, null, null);

        Label displayNameLabel = new Label(user.getDisplayName());

        Label usernameLabel = new Label("@" + user.getUsername());

        VBox userInformation = new VBox(4, displayNameLabel, usernameLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox row;

        if (user.getId() == Session.getCurrentUser().getId())
        {
            row = new HBox(12,avatar, userInformation, spacer);
        }
        else
        {
            Button followButton = new Button();
            followButton.getStyleClass().add("follow-btn");
            row = new HBox(12,avatar, userInformation, spacer, followButton);
            FollowUtil.checkFollowStatus(user, followButton);
            followButton.setOnAction(e -> { e.consume();FollowUtil.handleFollow(user, followButton);});
        }

        row.setOnMouseClicked(e -> openUserProfile(user));

        row.getStyleClass().add("follow-user-row");
        displayNameLabel.getStyleClass().add("follow-display-name");
        usernameLabel.getStyleClass().add("follow-username");
        avatar.getStyleClass().add("follow-avatar");

        return row;
    }

    private void openUserProfile(User user)
    {
        if (user.getId() == Session.getCurrentUser().getId())
        {
            Navigation.loadProfile();
        }
        else
        {
            Navigation.setSelectedUser(user);
            Navigation.loadOthersProfile();
        }
    }

    @FXML
    public void goBack()
    {
        User previousUser = Navigation.getFollowListUser();

        if (previousUser.getId() == Session.getCurrentUser().getId())
        {
            Navigation.loadProfile();
        }
        else
        {
            Navigation.setSelectedUser(previousUser);
            Navigation.loadOthersProfile();
        }
    }
}