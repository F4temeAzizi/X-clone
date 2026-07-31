package ap404.xclone.Shared.DTO.request;

import java.io.Serializable;

public class ReplyRequest implements Serializable {

    private int userId;
    private int tweetId;
    private String content;

    public ReplyRequest(int userId, int tweetId, String content) {
        this.userId = userId;
        this.tweetId = tweetId;
        this.content = content;
    }

    public int getUserId() { return userId; }

    public int getTweetId() { return tweetId; }

    public String getContent() { return content; }
}
