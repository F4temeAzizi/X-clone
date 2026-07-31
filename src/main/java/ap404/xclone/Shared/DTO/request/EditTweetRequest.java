package ap404.xclone.Shared.DTO.request;

import ap404.xclone.Shared.Models.Media;

import java.io.Serializable;
import java.util.List;

public class EditTweetRequest implements Serializable {

    private int tweetId;
    private int userId;
    private String content;
    private List<Media> media;

    public EditTweetRequest (int tweetId, int userId, String content, List<Media> media)
    {
        this.tweetId = tweetId;
        this.userId = userId;
        this.content = content;
        this.media = media;
    }

    public int getTweetId() { return tweetId; }
    public int getUserId() { return userId; }
    public String getContent() { return content; }
    public List<Media> getMedia() { return media; }
}
