package ap404.xclone.Shared.DTO.request;

import java.io.Serializable;

public class GetFollowersRequest implements Serializable
{
    private final int userId;

    public GetFollowersRequest(int userId)
    {
        this.userId = userId;
    }

    public int getUserId()
    {
        return userId;
    }
}