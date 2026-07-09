package ap404.xclone.Shared.DTO.request;

import java.io.Serializable;

public class GetTweetsByUserRequest  implements Serializable {
    private int userId;

    public GetTweetsByUserRequest (int userId)
    {
        this.userId = userId;
    }

    public int getUserId() { return  userId; }
}
