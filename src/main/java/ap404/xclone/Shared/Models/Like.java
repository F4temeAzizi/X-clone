package ap404.xclone.Shared.Models;

import java.io.Serializable;
import java.security.Timestamp;

public class Like implements Serializable {

    private int userId;
    private int tweetId;
    private Timestamp createdAt;

    public void Like(int userId, int tweetId, Timestamp createdAt) {
        this.userId = userId;
        this.tweetId = tweetId;
        this.createdAt = createdAt;
    }

    public int getUserId() { return userId; }
    public int getTweetId() { return tweetId; }
    public Timestamp getCreatedAt() { return createdAt; }
}
