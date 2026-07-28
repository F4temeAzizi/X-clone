package ap404.xclone.Client.Controllers;
import ap404.xclone.Client.Client;
import ap404.xclone.Client.Managers.Navigation;
import ap404.xclone.Client.Managers.Session;
import ap404.xclone.Client.Managers.ThemeManager;
import ap404.xclone.Client.Utils.FollowUtil;
import ap404.xclone.Client.Utils.UserUtil;
import ap404.xclone.Shared.DTO.enums.RequestType;
import ap404.xclone.Shared.DTO.enums.ResponseType;
import ap404.xclone.Shared.DTO.request.Request;
import ap404.xclone.Shared.DTO.request.SearchUsersRequest;
import ap404.xclone.Shared.DTO.response.Response;
import ap404.xclone.Shared.Models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainController
{
    @FXML private Button moreOptionsBtn;
    @FXML private StackPane center;
    @FXML private ImageView sidebarAvatar;
    @FXML private Label sidebarName;
    @FXML private Label sidebarUsername;
    @FXML private TextField searchField;
    @FXML private VBox searchResultContainer;

    private ContextMenu moreOptionsMenu;

    @FXML
    public void initialize()
    {
        Navigation.setCenter(center);
        Navigation.setMainController(this);
        updateUserProfile();
        Navigation.loadHome();

        moreOptionsMenu = new ContextMenu();
        moreOptionsMenu.getStyleClass().add("x-menu");
        MenuItem logoutItem = new MenuItem("Log Out");

        logoutItem.setOnAction(e -> {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

            alert.setTitle("Log Out");
            alert.setHeaderText("Are you sure you want to log out?");

            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(ThemeManager.getThemeCss());
            dialogPane.getStylesheets().add(getClass().getResource("/css/alert.css").toExternalForm());
            dialogPane.getStyleClass().add("alert");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isEmpty() || result.get() != ButtonType.OK) return;

            try {
                Session.setClient(null);
                Session.setCurrentUser(null);
                Navigation.navigate("login.fxml");
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        });

        moreOptionsMenu.getItems().add(logoutItem);

        searchField.textProperty().addListener(((obs, o, n) -> {
            if (n.isBlank())
            {
                searchResultContainer.getChildren().clear();
                return;
            }
            loadSearchResults(n);
        }));
    }

    @FXML public void goToProfile() { Navigation.loadProfile(); }

    @FXML public void goToHome() { Navigation.loadHome(); }

    @FXML public void goToExplore() { Navigation.loadExplore(); }

    @FXML public void goToBookmarks() { Navigation.loadBookmark(); }

    @FXML public void goToNotifications() {}

    @FXML public void  goToChat() {}

    @FXML public void goToSettings() { Navigation.loadSettings(); }

    @FXML public void showMoreOptions(ActionEvent e) {

        if(moreOptionsMenu.isShowing())
            moreOptionsMenu.hide();
        else {
            moreOptionsMenu.show(moreOptionsBtn, Side.TOP, 0, 0);
        }
    }

    public void updateUserProfile ()
    {
        UserUtil.loadUser(Session.getCurrentUser(),
                sidebarName, sidebarUsername,
                null, sidebarAvatar,
                null, null);
    }

    @FXML
    public void openPostPage()
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/post.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            ThemeManager.applyTheme(scene);
            stage.setScene(scene);
            stage.setTitle("Post");
            stage.setResizable(false);
            stage.showAndWait();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private List<User> searchUsers(String keyword)
    {
        try
        {
            Client client = Session.getClient();

            SearchUsersRequest searchUsersRequest = new SearchUsersRequest(keyword, Session.getCurrentUser().getId());

            Request request = new Request(RequestType.SEARCH_USERS, searchUsersRequest);

            client.sendRequest(request);

            Response response = client.getResponse();

            if (response.getType() == ResponseType.SEARCH_USERS_SUCCESS)
            {
                return (List<User>) response.getBody();
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        return new ArrayList<>();
    }

    private void loadSearchResults(String keyword)
    {
        searchResultContainer.getChildren().clear();

        List<User> users = searchUsers(keyword);

        for (User user : users)
        {
            searchResultContainer.getChildren().add(createUserRow(user));
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

        Button followButton = new Button();
        followButton.getStyleClass().add("follow-btn");

        HBox row = new HBox(12,avatar, userInformation, spacer, followButton);

        row.setOnMouseClicked(e -> openUserProfile(user));
        FollowUtil.checkFollowStatus(user, followButton);
        followButton.setOnAction(e -> { e.consume();FollowUtil.handleFollow(user, followButton);});

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

    public void refreshSearchResults()
    {
        String keyword = searchField.getText();
        if (!keyword.isBlank())loadSearchResults(keyword);
    }
}