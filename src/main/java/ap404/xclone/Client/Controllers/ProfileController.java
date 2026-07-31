package ap404.xclone.Client.Controllers;

import ap404.xclone.Client.Client;
import ap404.xclone.Client.Managers.Navigation;
import ap404.xclone.Client.Managers.Session;
import ap404.xclone.Client.Utils.TweetUtil;
import ap404.xclone.Client.Utils.UserUtil;
import ap404.xclone.Shared.DTO.enums.PageType;
import ap404.xclone.Shared.DTO.enums.RequestType;
import ap404.xclone.Shared.DTO.enums.ResponseType;
import ap404.xclone.Shared.DTO.request.GetProfileTweetsRequest;
import ap404.xclone.Shared.DTO.request.Request;
import ap404.xclone.Shared.DTO.response.Response;
import ap404.xclone.Shared.Models.Tweet;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import java.util.List;
import ap404.xclone.Shared.DTO.request.GetFollowCountsRequest;
import ap404.xclone.Shared.Models.FollowCounts;

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
    @FXML private Label likesTab;
    @FXML private VBox tweetContainer;
    @FXML private ScrollPane profileScroll;
    @FXML private Label followingLabel;
    @FXML private Label followersLabel;


    public void initialize ()
    {
        Navigation.setPreviousPage(PageType.PROFILE);
        UserUtil.loadUser(Session.getCurrentUser(), nameLbl, usernameLbl, bioLbl, avatarImage, bannerRegion, createdAtLbl);
        loadFollowCounts();
        switch (Navigation.getProfileTab())
        {
            case "replies":
                showReplies();
                break;

            case "likes":
                showLikes();
                break;

            default:
                showPosts();
                break;
        }
        profileScroll.layout();
        profileScroll.setVvalue(Navigation.getProfileScroll());
        profileScroll.vvalueProperty().addListener((obs, o, n) ->
                Navigation.setProfileScroll(n.doubleValue()));
    }

    @FXML public void goToEditProfile() { Navigation.loadEditProfile(); }

    @FXML public void showPosts ()
    {
        Navigation.setProfileTab("posts");
        selectTab(postsTab);

        try
        {
            Client client = Session.getClient();

            GetProfileTweetsRequest getProfileTweetsRequest = new GetProfileTweetsRequest(Session.getCurrentUser().getId(), Session.getCurrentUser().getId());

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
            System.err.println("Failed to load tweets: " + e.getMessage());
        }

    }

    @FXML public void showReplies ()
    {
        Navigation.setProfileTab("replies");
        selectTab(repliesTab);

        try {
            Client client = Session.getClient();

            GetProfileTweetsRequest getProfileTweetsRequest = new GetProfileTweetsRequest(
                    Session.getCurrentUser().getId(),
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
        Navigation.setProfileTab("likes");
        selectTab(likesTab);

        try
        {
            Client client = Session.getClient();

           GetProfileTweetsRequest getLikedTweetsRequest = new GetProfileTweetsRequest(
                   Session.getCurrentUser().getId(),
                   Session.getCurrentUser().getId()
           );

            client.sendRequest(new Request(RequestType.GET_LIKED_TWEETS, getLikedTweetsRequest));

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
    private void loadFollowCounts()
    {
        try
        {
            Client client = Session.getClient();

            GetFollowCountsRequest requestBody = new GetFollowCountsRequest(Session.getCurrentUser().getId());

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
        Navigation.setFollowListUser(Session.getCurrentUser());

        Navigation.setFollowListType("following");

        Navigation.loadFollowList();
    }

    @FXML
    public void showFollowers()
    {
        Navigation.setFollowListUser(Session.getCurrentUser());

        Navigation.setFollowListType("followers");

        Navigation.loadFollowList();
    }
}

