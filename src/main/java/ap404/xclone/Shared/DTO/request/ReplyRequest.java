package ap404.xclone.Shared.DTO.request;

import java.io.Serializable;

public class ReplyRequest implements Serializable {

    private int userId;
    private int tweetId;

    public ReplyRequest(int userId, int tweetId) {
        this.userId = userId;
        this.tweetId = tweetId;
    }

    public int getUserId() { return userId; }

    public int getTweetId() { return tweetId; }
}
