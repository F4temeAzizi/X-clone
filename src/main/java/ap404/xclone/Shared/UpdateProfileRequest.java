package ap404.xclone.Shared;

import java.io.Serializable;

public class UpdateProfileRequest implements Serializable {
    private int id;
    private String name;
    private String username;
    private String bio;
    private String bannerImage;
    private String avatarImage;

    public UpdateProfileRequest (int id, String name, String username, String bio, String bannerImage, String avatarImage)
    {
        this.id = id;
        this.name = name;
        this.username = username;
        this.bio = bio;
        this.bannerImage = bannerImage;
        this.avatarImage = avatarImage;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getUsername() { return username; }
    public String getBio() { return bio; }
    public String getBannerImage() { return bannerImage; }
    public String getAvatarImage() { return avatarImage; }
}
