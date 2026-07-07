module ap404.xclone {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens ap404.xclone.Client.Controllers to javafx.fxml;
    exports ap404.xclone.Client;
    exports ap404.xclone.Client.Utils;
}