module ap404.xclone {
    requires javafx.controls;
    requires javafx.fxml;

    exports ap404.xclone.Client.Controllers;
    opens ap404.xclone.Client.Controllers to javafx.fxml;
}