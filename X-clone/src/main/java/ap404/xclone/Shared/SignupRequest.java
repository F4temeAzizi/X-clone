package ap404.xclone.Shared;

import java.io.Serializable;

public class SignupRequest implements Serializable {

    private String username;
    private String password;
    private String email;

    public SignupRequest(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
}
