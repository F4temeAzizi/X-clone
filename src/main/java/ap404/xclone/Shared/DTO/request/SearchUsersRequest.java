package ap404.xclone.Shared.DTO.request;

import java.io.Serializable;

public class SearchUsersRequest implements Serializable
{
    private String keyword;
    private int currentUserId;

    public SearchUsersRequest(String keyword, int currentUserId)
    {
        this.keyword = keyword;
        this.currentUserId = currentUserId;
    }

    public String getKeyword() { return keyword; }
    public int getCurrentUserId() { return currentUserId; }
}
