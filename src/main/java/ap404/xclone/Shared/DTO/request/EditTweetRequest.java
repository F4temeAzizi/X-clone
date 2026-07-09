package ap404.xclone.Shared.DTO.request;

import java.io.Serializable;

public class EditTweetRequest implements Serializable {

    private int tweetId;
    private int userId;
    private String content;

    public EditTweetRequest (int tweetId, int userId, String content)
    {
        this.tweetId = tweetId;
        this.userId = userId;
        this.content = content;
    }

    public int getTweetId() { return tweetId; }

    public int getUserId() { return userId; }

    public String getContent() { return content; }
}
