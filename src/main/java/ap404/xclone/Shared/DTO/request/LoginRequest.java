package ap404.xclone.Shared.DTO.request;

import java.io.Serializable;

public class LoginRequest implements Serializable {

    private String username;
    private String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }

}
