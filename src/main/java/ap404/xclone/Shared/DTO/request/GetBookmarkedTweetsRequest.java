package ap404.xclone.Shared.DTO.request;

import java.io.Serializable;

public class GetBookmarkedTweetsRequest implements Serializable
{
    private final int userId;
    private final int currentUserId;

    public GetBookmarkedTweetsRequest(int userId, int currentUserId)
    {
        this.userId = userId;
        this.currentUserId = currentUserId;
    }

    public int getUserId() { return userId; }
    public int getCurrentUserId() { return currentUserId; }
}