package ap404.xclone.Client.Managers;

import ap404.xclone.Client.Controllers.ExploreController;
import ap404.xclone.Client.Controllers.HomeController;
import ap404.xclone.Client.Controllers.MainController;
import ap404.xclone.Client.Controllers.OthersProfileController;
import ap404.xclone.Client.XApplication;
import ap404.xclone.Shared.DTO.enums.PageType;
import ap404.xclone.Shared.Models.Tweet;
import ap404.xclone.Shared.Models.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Navigation
{
    private static StackPane center;
    private static MainController mainController;
    private static HomeController homeController;
    private static ExploreController exploreController;
    private static OthersProfileController othersProfileController;
    private static User selectedUser;
    private static Tweet selectedTweet;

    private static double homeScroll = 0;
    private static double exploreScroll = 0;
    private static double bookmarkScroll = 0;
    private static double profileScroll = 0;
    private static String profileTab = "posts";
    private static List<Tweet> replyHistory = new ArrayList<>();

    private static User followListUser;
    private static String followListType;

    private static String composeText = "";
    private static String selectedHashtag;
    private static String searchTweetText = "";

    private static PageType previousPage;
    private static PageType profileReturnPage;
    private static Tweet profileReturnTweet;


    public static void setPreviousPage(PageType page) {
        previousPage = page;
    }
    public static PageType getPreviousPage() { return previousPage; }

    public static PageType getProfileReturnPage() { return profileReturnPage; }
    public static void setProfileReturnPage(PageType page) { profileReturnPage = page; }

    public static Tweet getProfileReturnTweet() { return profileReturnTweet; }
    public static void setProfileReturnTweet(Tweet profileReturnTweet) { Navigation.profileReturnTweet = profileReturnTweet; }


    public static void loadHome() { load("home.fxml"); }
    public static void loadProfile() { load("profile.fxml"); }
    public static void loadEditProfile() { load("edit-profile.fxml"); }
    public static void loadExplore() { load("explore.fxml"); }
    public static void loadBookmark() { load("bookmark.fxml"); }
    public static void loadOthersProfile() { load("others-profile.fxml"); }
    public static void loadSettings() { load("settings.fxml"); }
    public static void loadTheme() { load("theme.fxml"); }
    public static void loadChangePassword() { load("change-password.fxml"); }
    public static void loadPrivacy() { load("privacy.fxml"); }
    public static void loadDeleteAccount() { load("delete-account.fxml"); }
    public static void loadShowReplies() { load("show-replies.fxml"); }
    public static void loadFollowList() {load("follow-list.fxml");}
    public static void loadLogin() { load("login.fxml");}

    public static void addReplyToHistory( Tweet tweet) { replyHistory.add(tweet); }
    public static void clearHistory() {
        replyHistory.clear();
    }


    public static void removeLastReply()
    {
        if (replyHistory.isEmpty() ) return;
        replyHistory.remove(replyHistory.size() - 1);
    }

    public static Tweet getCurrentReply()
    {
        if (replyHistory.isEmpty()) return null;
        return replyHistory.get(replyHistory.size() - 1);
    }

    public static void showHashtag(String hashtag)
    {
        setSelectedHashtag(hashtag);
        loadExplore();
    }


    public static User getSelectedUser() { return selectedUser; }
    public static void setSelectedUser(User selectedUser) { Navigation.selectedUser = selectedUser;}

    public static Tweet getSelectedTweet() { return selectedTweet; }
    public static void setSelectedTweet(Tweet selectedTweet) { Navigation.selectedTweet = selectedTweet; }

    public static void setMainController(MainController controller) { mainController = controller; }
    public static MainController getMainController() { return mainController; }

    public static void setHomeController(HomeController controller) { homeController = controller;}
    public static HomeController getHomeController() { return homeController; }

    public static void setExploreController(ExploreController controller) { exploreController = controller; }
    public static ExploreController getExploreController() { return exploreController; }

    public static void setOthersProfileController(OthersProfileController controller) { othersProfileController = controller; }
    public static OthersProfileController getOthersProfileController() { return othersProfileController; }

    public static double getHomeScroll() { return homeScroll; }
    public static void setHomeScroll(double value) { homeScroll = value; }

    public static double getExploreScroll() { return exploreScroll; }
    public static void setExploreScroll(double value) { exploreScroll = value; }

    public static double getBookmarkScroll() { return bookmarkScroll; }
    public static void setBookmarkScroll(double value) { bookmarkScroll = value; }

    public static double getProfileScroll() { return profileScroll; }
    public static void setProfileScroll(double value) { profileScroll = value; }

    public static String getProfileTab() { return profileTab; }
    public static void setProfileTab(String tab) { profileTab = tab; }

    public static String getComposeText() { return composeText; }
    public static void setComposeText(String text) { composeText = text; }

    public static String getSearchTweetText() { return searchTweetText; }
    public static void setSearchTweetText(String searchTweetText) { Navigation.searchTweetText = searchTweetText; }

    public static String getSelectedHashtag() { return selectedHashtag; }
    public static void setSelectedHashtag(String hashtag) { selectedHashtag = hashtag; }

    public static void setCenter(StackPane centerPane)
    {
        center = centerPane;
    }

    public static User getFollowListUser() { return followListUser; }
    public static void setFollowListUser(User user) { followListUser = user; }

    public static String getFollowListType() { return followListType; }
    public static void setFollowListType(String type) { followListType = type; }


    public static void load(String fxml)
    {
        try
        {
            Parent root = FXMLLoader.load(Navigation.class.getResource("/" + fxml));
            center.getChildren().setAll(root);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void navigate(String fxml) throws IOException {
        Parent root = FXMLLoader.load(Navigation.class.getResource("/" + fxml));
        XApplication.getPrimaryStage().getScene().setRoot(root);
    }

    public static void clear()
    {
        selectedUser = null;
        selectedTweet = null;

        homeController = null;
        exploreController = null;
        othersProfileController = null;
        mainController = null;

        followListUser = null;
        followListType = null;

        selectedHashtag = null;

        previousPage = null;
        profileReturnPage = null;
        profileReturnTweet = null;

        homeScroll = 0;
        exploreScroll = 0;
        bookmarkScroll = 0;
        profileScroll = 0;

        profileTab = "posts";
        composeText = "";
        searchTweetText = "";

        replyHistory.clear();
    }
}