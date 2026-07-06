package ap404.xclone.Shared;

public class UpdateProfileRequest {
    private String name;
    private String username;
    private String bio;
    private String bannerImage;
    private String avatarImage;

    public UpdateProfileRequest(String name, String username, String bio, String bannerImage, String avatarImage)
    {
        this.name = name;
        this.username = username;
        this.bio = bio;
        this.bannerImage = bannerImage;
        this.avatarImage = avatarImage;
    }

    public String getName() { return name; }
    public String getUsername() { return username; }
    public String getBio() { return bio; }
    public String getBannerImage() { return bannerImage; }
    public String getAvatarImage() { return avatarImage; }
}
