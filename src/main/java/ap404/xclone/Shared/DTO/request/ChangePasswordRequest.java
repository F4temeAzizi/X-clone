package ap404.xclone.Shared.DTO.request;

import java.io.Serializable;

public class ChangePasswordRequest implements Serializable {

    private int userId;
    private String currentPassword;
    private String newPassword;

    public ChangePasswordRequest(int userId, String currentPassword, String newPassword) {
        this.userId = userId;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    public int getUserId() {
        return userId;
    }
    public String getCurrentPassword() {
        return currentPassword;
    }
    public String getNewPassword() {
        return newPassword;
    }
}
