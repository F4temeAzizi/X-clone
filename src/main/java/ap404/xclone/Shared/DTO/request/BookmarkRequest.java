package ap404.xclone.Shared.DTO.request;

import java.io.Serializable;

public class BookmarkRequest implements Serializable
{
    private final int userId;
    private final int tweetId;

    public BookmarkRequest(int userId, int tweetId)
    {
        this.userId = userId;
        this.tweetId = tweetId;
    }

    public int getUserId() { return userId; }
    public int getTweetId() { return tweetId; }
}
