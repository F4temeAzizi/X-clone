package ap404.xclone.Client.Utils;
import javafx.scene.control.Label;

public class MessageUtil
{
    public static void showError(Label label, String message) {

        if (label == null) return;
        if (message == null || message.trim().isEmpty()) {
            label.setVisible(false);
            label.setText("");
            return;
        }

        label.getStyleClass().removeAll("success-label", "error-label");
        label.getStyleClass().add("error-label");
        label.setText(message);
        label.setVisible(true);
    }

    public static void showSuccess(Label label, String message) {

        if (label == null) return;
        if (message == null || message.trim().isEmpty()) {
            label.setVisible(false);
            label.setText("");
            return;
        }

        label.getStyleClass().removeAll("success-label", "error-label");
        label.getStyleClass().add("success-label");
        label.setText(message);
        label.setVisible(true);
    }
}