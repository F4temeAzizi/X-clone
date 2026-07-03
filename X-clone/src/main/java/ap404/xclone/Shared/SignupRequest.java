package ap404.xclone.Shared;

import java.io.Serializable;

public class SignupRequest implements Serializable {

    private String name;
    private String username;
    private String password;
    private String email;

    public SignupRequest(String name, String username, String password, String email) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getName() { return name; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
}
