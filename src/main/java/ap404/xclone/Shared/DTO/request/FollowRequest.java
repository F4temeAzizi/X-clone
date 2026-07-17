package ap404.xclone.Shared.DTO.request;

import java.io.Serializable;

public class FollowRequest implements Serializable {

    private final int followerId;
    private final int followingId;

    public FollowRequest(int followerId, int followingId) {
        this.followerId = followerId;
        this.followingId = followingId;
    }

    public int getFollowerId() {
        return followerId;
    }

    public int getFollowingId() {
        return followingId;
    }
}