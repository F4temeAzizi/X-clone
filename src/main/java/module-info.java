module ap404.xclone {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;
    requires javafx.media;

    opens ap404.xclone.Client.Controllers to javafx.fxml;
    exports ap404.xclone.Client;
    exports ap404.xclone.Client.Utils;
}