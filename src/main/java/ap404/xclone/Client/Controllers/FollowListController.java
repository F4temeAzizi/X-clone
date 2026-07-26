package ap404.xclone.Client.Controllers;

import ap404.xclone.Client.Client;
import ap404.xclone.Client.Managers.Navigation;
import ap404.xclone.Client.Managers.Session;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

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
        Label displayNameLabel =
                new Label(user.getDisplayName());

        displayNameLabel.setStyle(
                "-fx-font-weight: bold;"
        );

        Label usernameLabel =
                new Label("@" + user.getUsername());

        VBox userInformation = new VBox(
                4,
                displayNameLabel,
                usernameLabel
        );

        Region space = new Region();
        HBox.setHgrow(space, Priority.ALWAYS);

        Button profileButton = new Button("View profile");

        profileButton.setOnAction(event ->
                openUserProfile(user)
        );

        HBox row = new HBox(
                12,
                userInformation,
                space,
                profileButton
        );

        row.setStyle("-fx-padding: 12;" + "-fx-alignment: center-left;" + "-fx-border-color: " + "transparent transparent " + "#dddddd transparent;");

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