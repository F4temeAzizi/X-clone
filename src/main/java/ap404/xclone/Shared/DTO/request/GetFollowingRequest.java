package ap404.xclone.Shared.DTO.request;

import java.io.Serializable;

public class GetFollowingRequest implements Serializable
{
    private final int userId;

    public GetFollowingRequest(int userId)
    {
        this.userId = userId;
    }

    public int getUserId()
    {
        return userId;
    }
}