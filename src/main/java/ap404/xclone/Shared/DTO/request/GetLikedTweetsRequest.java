package ap404.xclone.Shared.DTO.request;

import java.io.Serializable;

public class GetLikedTweetsRequest implements Serializable {
    private int userId;
    private int currentUserId;

    public GetLikedTweetsRequest(int userId, int currentUserId) {
        this.userId = userId;
        this.currentUserId = currentUserId;
    }

    public int getUserId() { return  userId; }
    public int getCurrentUserId() { return currentUserId; }
}
