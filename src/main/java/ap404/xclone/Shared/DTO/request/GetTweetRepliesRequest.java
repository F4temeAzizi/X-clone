package ap404.xclone.Shared.DTO.request;

import java.io.Serializable;

public class GetTweetRepliesRequest implements Serializable {

    private int tweetId;
    private int currentUserId;

    public GetTweetRepliesRequest(int tweetId, int currentUserId) {
        this.tweetId = tweetId;
        this.currentUserId = currentUserId;
    }

    public int getTweetId() { return tweetId; }
    public int getCurrentUserId() { return currentUserId; }
}
