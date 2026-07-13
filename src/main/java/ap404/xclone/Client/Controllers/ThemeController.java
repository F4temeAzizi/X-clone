package ap404.xclone.Client.Controllers;

import ap404.xclone.Client.Managers.ThemeManager;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;

public class ThemeController
{
    @FXML private RadioButton darkRadio;
    @FXML private RadioButton lightRadio;

    @FXML
    public void initialize()
    {
        darkRadio.setSelected(ThemeManager.isDarkTheme());
        lightRadio.setSelected(!ThemeManager.isDarkTheme());

        darkRadio.setOnAction(e ->
        {
            ThemeManager.setIsDark(true);
            ThemeManager.applyTheme(darkRadio.getScene());
        });

        lightRadio.setOnAction(e ->
        {
            ThemeManager.setIsDark(false);
            ThemeManager.applyTheme(lightRadio.getScene());
        });
    }
}
