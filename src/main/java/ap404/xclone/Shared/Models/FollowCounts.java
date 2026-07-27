package ap404.xclone.Shared.Models;

import java.io.Serializable;

public class FollowCounts implements Serializable {

    private int followersCount;
    private int followingCount;

    public FollowCounts(int followersCount, int followingCount) {
        this.followersCount = followersCount;
        this.followingCount = followingCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }
}