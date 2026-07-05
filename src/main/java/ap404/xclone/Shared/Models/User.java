package ap404.xclone.Shared.Models;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String username;
    private String displayName;
    private String email;
    private String passwordHash;
    private String bio;
    private String profileImageUrl;
    private String bannerImageUrl;
    private boolean isPrivate;

    public User (int id, String username, String displayName, String email,
                String passwordHash, String bio, String profileImageUrl,
                String bannerImageUrl, boolean isPrivate) {
        this.id = id;
        this.username = username;
        this.displayName = displayName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.bio = bio;
        this.profileImageUrl = profileImageUrl;
        this.bannerImageUrl = bannerImageUrl;
        this.isPrivate = isPrivate;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getDisplayName() { return displayName; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getBio() { return bio; }
    public String getProfileImageUrl() { return profileImageUrl; }
    public String getBannerImageUrl() { return bannerImageUrl; }
    public boolean isPrivate() { return isPrivate; }
}