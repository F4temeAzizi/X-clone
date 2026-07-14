package ap404.xclone.Client.Controllers;

import ap404.xclone.Client.Managers.Navigation;
import javafx.fxml.FXML;

public class SettingsController
{
    @FXML
    public void openTheme()
    {
        Navigation.loadTheme();
    }

    @FXML
    public void openChangePassword() { Navigation.loadChangePassword(); }

    @FXML
    public void openPrivacy() { Navigation.loadPrivacy(); }

    @FXML
    public void openDeleteAccount() { Navigation.loadDeleteAccount();}
}
