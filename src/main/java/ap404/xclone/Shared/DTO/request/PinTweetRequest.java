package ap404.xclone.Shared.DTO.request;

import java.io.Serializable;

public class PinTweetRequest implements Serializable {

    private final int tweetId;
    private final int userId;

    public PinTweetRequest(int userId, int tweetId) {
        this.userId = userId;
        this.tweetId = tweetId;
    }

    public int getUserId() { return userId; }
    public int getTweetId() { return tweetId; }
}
