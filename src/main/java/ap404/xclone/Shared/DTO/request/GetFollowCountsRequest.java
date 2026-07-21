package ap404.xclone.Shared.DTO.request;

import java.io.Serializable;

public class GetFollowCountsRequest implements Serializable {

    private int userId;

    public GetFollowCountsRequest(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }
}