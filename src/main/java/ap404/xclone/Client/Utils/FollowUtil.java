package ap404.xclone.Client.Utils;

import ap404.xclone.Client.Client;
import ap404.xclone.Client.Managers.Navigation;
import ap404.xclone.Client.Managers.Session;
import ap404.xclone.Shared.DTO.enums.RequestType;
import ap404.xclone.Shared.DTO.enums.ResponseType;
import ap404.xclone.Shared.DTO.request.FollowRequest;
import ap404.xclone.Shared.DTO.request.Request;
import ap404.xclone.Shared.DTO.response.Response;
import ap404.xclone.Shared.Models.User;
import javafx.scene.control.Button;

public class FollowUtil {

    public static void updateFollowButton(Button followButton, boolean following) {
        if (following) followButton.setText("Following");
        else followButton.setText("Follow");
    }

    public static void checkFollowStatus(User user, Button followButton) {
        try {
            Client client = Session.getClient();

            FollowRequest followRequest = new FollowRequest(
                    Session.getCurrentUser().getId(),
                    user.getId()
            );

            client.sendRequest(new Request(RequestType.CHECK_FOLLOW, followRequest));

            Response response = client.getResponse();

            if (response.getType() == ResponseType.CHECK_FOLLOW_SUCCESS) {

                boolean following = (Boolean) response.getBody();
                updateFollowButton(followButton, following);
            }
        }
        catch (Exception e) {
            System.out.println("Check follow status failed: " + e.getMessage());
        }
    }

    public static void followUser(User user, Button followButton) {
        try {
            Client client = Session.getClient();

            FollowRequest followRequest = new FollowRequest(
                    Session.getCurrentUser().getId(),
                    user.getId()
            );

            client.sendRequest(new Request(RequestType.FOLLOW, followRequest));

            Response response = client.getResponse();

            if (response.getType() == ResponseType.FOLLOW_SUCCESS) {
                updateFollowButton(followButton, true);
                if (Navigation.getOthersProfileController() != null) {
                    Navigation.getOthersProfileController().refreshFollowStatus();
                }
                if (Navigation.getMainController() != null) {
                    Navigation.getMainController().refreshSearchResults();
                }
            }
        } catch (Exception e) {
            System.out.println("Follow failed: " + e.getMessage());
        }
    }

    public static void unfollowUser(User user, Button followButton) {
        try {
            Client client = Session.getClient();

            FollowRequest followRequest = new FollowRequest(
                    Session.getCurrentUser().getId(),
                    user.getId()
            );

            client.sendRequest(new Request(RequestType.UNFOLLOW, followRequest));

            Response response = client.getResponse();

            if (response.getType() == ResponseType.UNFOLLOW_SUCCESS) {
                updateFollowButton(followButton, false);
                if (Navigation.getOthersProfileController() != null) {
                    Navigation.getOthersProfileController().refreshFollowStatus();
                }
                if (Navigation.getMainController() != null) {
                    Navigation.getMainController().refreshSearchResults();
                }
            }
        } catch (Exception e) {
            System.out.println("Unfollow failed: " + e.getMessage());
        }
    }

    public static void handleFollow(User user, Button followButton) {
        if (followButton.getText().equals("Following")) unfollowUser(user, followButton);
        else followUser(user, followButton);
    }
}
