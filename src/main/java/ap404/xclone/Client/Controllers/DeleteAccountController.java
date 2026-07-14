package ap404.xclone.Client.Controllers;

import ap404.xclone.Client.Managers.Navigation;
import javafx.fxml.FXML;

public class DeleteAccountController
{
    @FXML public void handleBack() { Navigation.loadSettings(); }
    @FXML public void cancel () { Navigation.loadSettings(); }
}
