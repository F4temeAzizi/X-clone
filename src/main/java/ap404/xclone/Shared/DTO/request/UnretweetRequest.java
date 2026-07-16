package ap404.xclone.Shared.DTO.request;

import java.io.Serializable;

public class UnretweetRequest implements Serializable {

    int userId;
    int rootTweetId;

    public UnretweetRequest(int userId, int rootTweetId) {
        this.userId = userId;
        this.rootTweetId = rootTweetId;
    }

    public int getUserId() { return userId; }
    public int getRootTweetId() { return rootTweetId; }
}
