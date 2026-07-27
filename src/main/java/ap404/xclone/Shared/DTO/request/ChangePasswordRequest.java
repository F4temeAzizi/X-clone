package ap404.xclone.Shared.DTO.request;

import java.io.Serializable;

public class ChangePasswordRequest implements Serializable {

    private int userId;
    private int currentPassword;
    private int newPassword;

    public ChangePasswordRequest(int userId, int currentPassword, int newPassword) {
        this.userId = userId;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    public int getUserId() {
        return userId;
    }
    public int getCurrentPassword() {
        return currentPassword;
    }
    public int getNewPassword() {
        return newPassword;
    }
}
