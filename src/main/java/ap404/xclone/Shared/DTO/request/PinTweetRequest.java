package ap404.xclone.Shared.DTO.request;

import java.io.Serializable;

public class PinTweetRequest implements Serializable {

    private final Integer tweetId;
    private final int userId;

    public PinTweetRequest(int userId, Integer tweetId) {
        this.userId = userId;
        this.tweetId = tweetId;
    }

    public int getUserId() { return userId; }
    public Integer getTweetId() { return tweetId; }
}
