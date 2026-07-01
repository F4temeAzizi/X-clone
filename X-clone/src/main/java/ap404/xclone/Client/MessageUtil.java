package ap404.xclone.Client;
import javafx.scene.control.Label;

public class MessageUtil
{
    public static void setErrorMessage(Label label, String message)
    {
        if (label == null) return;
        if (message == null || message.trim().isEmpty())
        {
            label.setVisible(false);
            label.setText("");
        }
        else
        {
            label.setText(message);
            label.setVisible(true);
        }
    }
}