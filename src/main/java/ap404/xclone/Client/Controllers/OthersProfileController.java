package ap404.xclone.Client.Controllers;

import ap404.xclone.Client.Client;
import ap404.xclone.Client.Managers.Navigation;
import ap404.xclone.Client.Managers.Session;
import ap404.xclone.Client.Utils.TweetUtil;
import ap404.xclone.Client.Utils.UserUtil;
import ap404.xclone.Shared.DTO.enums.RequestType;
import ap404.xclone.Shared.DTO.enums.ResponseType;
import ap404.xclone.Shared.DTO.request.GetProfileTweetsRequest;
import ap404.xclone.Shared.DTO.request.Request;
import ap404.xclone.Shared.DTO.response.Response;
import ap404.xclone.Shared.Models.Tweet;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.List;
import ap404.xclone.Shared.DTO.request.FollowRequest;
import javafx.scene.control.Button;
import ap404.xclone.Shared.DTO.request.GetFollowCountsRequest;
import ap404.xclone.Shared.Models.FollowCounts;

public class OthersProfileController
{
    @FXML private Label nameLbl;
    @FXML private Label usernameLbl;
    @FXML private Label bioLbl;
    @FXML private ImageView avatarImage;
    @FXML private Region bannerRegion;
    @FXML private Label createdAtLbl;
    @FXML private Label postsTab;
    @FXML private Label repliesTab;
    @FXML private Label likesTab;
    @FXML private VBox tweetContainer;
    @FXML private Button followButton;
    private boolean following;
    @FXML private Label followingLabel;
    @FXML private Label followersLabel;


    public void initialize ()
    {
        UserUtil.loadUser(Navigation.getSelectedUser(), nameLbl, usernameLbl, bioLbl, avatarImage, bannerRegion, createdAtLbl);
        checkFollowStatus();
        loadFollowCounts();
        showPosts();
    }


    @FXML public void showPosts ()
    {
        selectTab(postsTab);

        try
        {
            Client client = Session.getClient();

            GetProfileTweetsRequest getProfileTweetsRequest= new GetProfileTweetsRequest(Navigation.getSelectedUser().getId(), Session.getCurrentUser().getId());

            client.sendRequest(new Request(RequestType.GET_TWEETS_BY_USER, getProfileTweetsRequest));

            Response response = client.getResponse();

            if (response.getType() == ResponseType.GET_TWEETS_BY_USER_SUCCESS)
            {
                List<Tweet> tweets = (List<Tweet>) response.getBody();
                TweetUtil.loadTweets(tweetContainer, tweets);
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

    }

    @FXML public void showReplies ()
    {
        selectTab(repliesTab);

        try {

            Client client = Session.getClient();

            GetProfileTweetsRequest getProfileTweetsRequest = new GetProfileTweetsRequest(
                    Navigation.getSelectedUser().getId(),
                    Session.getCurrentUser().getId()
            );

            client.sendRequest(new Request(RequestType.GET_USER_REPLIES, getProfileTweetsRequest));

            Response response = client.getResponse();

            if (response.getType() == ResponseType.GET_USER_REPLIES_SUCCESS) {
                List<Tweet> replies = (List<Tweet>) response.getBody();
                TweetUtil.loadTweets(tweetContainer, replies);
            }

        } catch (Exception e) {
            System.out.println("Failed to load replies: " + e.getMessage());
        }
    }

    @FXML public void showLikes ()
    {
        selectTab(likesTab);

        try
        {
            Client client = Session.getClient();

            GetProfileTweetsRequest getProfileTweetsRequest = new GetProfileTweetsRequest(
                    Navigation.getSelectedUser().getId(),
                    Session.getCurrentUser().getId()
            );

            client.sendRequest(new Request(RequestType.GET_LIKED_TWEETS, getProfileTweetsRequest));

            Response response = client.getResponse();

            if (response.getType() == ResponseType.GET_LIKED_TWEETS_SUCCESS) {
                List<Tweet> tweets = (List<Tweet>) response.getBody();
                TweetUtil.loadTweets(tweetContainer, tweets);
            }
        }
        catch (Exception e)
        {
            System.err.println("Failed to load liked tweets: " + e.getMessage());
        }
    }

    private void selectTab(Label active)
    {
        postsTab.getStyleClass().setAll("profile-tab");
        repliesTab.getStyleClass().setAll("profile-tab");
        likesTab.getStyleClass().setAll("profile-tab");

        active.getStyleClass().setAll("profile-tab-active");
    }

    private void checkFollowStatus() {

        try {
            Client client = Session.getClient();

            FollowRequest followRequest = new FollowRequest(
                    Session.getCurrentUser().getId(),
                    Navigation.getSelectedUser().getId()
            );

            client.sendRequest(
                    new Request(RequestType.CHECK_FOLLOW, followRequest)
            );

            Response response = client.getResponse();

            if (response.getType() == ResponseType.CHECK_FOLLOW_SUCCESS) {

                following = (Boolean) response.getBody();

                updateFollowButton();
            }

        } catch (Exception e) {
            System.out.println(
                    "Check follow status failed: " + e.getMessage()
            );
        }
    }
    @FXML
    public void handleFollow() {

        if (following) {
            unfollowUser();
        } else {
            followUser();
        }
    }

    private void followUser() {

        try {
            Client client = Session.getClient();

            FollowRequest followRequest = new FollowRequest(
                    Session.getCurrentUser().getId(),
                    Navigation.getSelectedUser().getId()
            );

            client.sendRequest(
                    new Request(RequestType.FOLLOW, followRequest)
            );

            Response response = client.getResponse();

            if (response.getType() == ResponseType.FOLLOW_SUCCESS) {

                following = true;
                updateFollowButton();
                loadFollowCounts();
            }

        } catch (Exception e) {
            System.out.println("Follow failed: " + e.getMessage());
        }
    }
    private void unfollowUser() {

        try {
            Client client = Session.getClient();

            FollowRequest followRequest = new FollowRequest(
                    Session.getCurrentUser().getId(),
                    Navigation.getSelectedUser().getId()
            );

            client.sendRequest(
                    new Request(RequestType.UNFOLLOW, followRequest)
            );

            Response response = client.getResponse();

            if (response.getType() == ResponseType.UNFOLLOW_SUCCESS) {

                following = false;
                updateFollowButton();
                loadFollowCounts();
            }

        } catch (Exception e) {
            System.out.println("Unfollow failed: " + e.getMessage());
        }
    }
    private void updateFollowButton() {

        if (following) {
            followButton.setText("Following");
        } else {
            followButton.setText("Follow");
        }
    }
    private void loadFollowCounts()
    {
        try
        {
            Client client = Session.getClient();

            GetFollowCountsRequest requestBody =
                    new GetFollowCountsRequest(
                            Navigation.getSelectedUser().getId()
                    );

            client.sendRequest(new Request(RequestType.GET_FOLLOW_COUNTS, requestBody));

            Response response = client.getResponse();

            if (response.getType() == ResponseType.GET_FOLLOW_COUNTS_SUCCESS)
            {
                FollowCounts counts = (FollowCounts) response.getBody();

                followingLabel.setText(counts.getFollowingCount() + " Following");

                followersLabel.setText(counts.getFollowersCount() + " Followers");
            }
        }
        catch (Exception e)
        {
            System.err.println(
                    "Failed to load follow counts: "
                            + e.getMessage()
            );
        }
    }

    @FXML
    public void showFollowing()
    {
        Navigation.setFollowListUser(Navigation.getSelectedUser());

        Navigation.setFollowListType("following");

        Navigation.loadFollowList();
    }

    @FXML
    public void showFollowers()
    {
        Navigation.setFollowListUser(Navigation.getSelectedUser());

        Navigation.setFollowListType("followers");

        Navigation.loadFollowList();
    }
}
