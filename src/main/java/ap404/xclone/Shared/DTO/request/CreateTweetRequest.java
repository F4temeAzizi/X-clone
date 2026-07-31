package ap404.xclone.Shared.DTO.request;

import ap404.xclone.Shared.Models.Media;

import java.io.Serializable;
import java.util.List;

public class CreateTweetRequest implements Serializable {
    private int userId;
    private String content;
    private List<Media> media;

    public CreateTweetRequest(int userId, String content, List<Media> media) {
        this.userId = userId;
        this.content = content;
        this.media = media;
    }

    public int getUserId() {
        return userId;
    }
    public String getContent() {
        return content;
    }
    public List<Media> getMedia() { return media; }
}