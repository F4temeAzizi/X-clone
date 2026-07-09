package ap404.xclone.Shared.DTO.request;

import java.io.Serializable;

public class DeleteTweetRequest implements Serializable {
    private int tweetId;
    private int userId;

    public DeleteTweetRequest (int tweetId, int userId)
    {
        this.tweetId = tweetId;
        this.userId = userId;
    }

    public int getTweetId() { return tweetId; }

    public int getUserId() { return userId; }
}
