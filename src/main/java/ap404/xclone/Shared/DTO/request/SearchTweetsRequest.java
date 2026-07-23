package ap404.xclone.Shared.DTO.request;

import java.io.Serializable;

public class SearchTweetsRequest implements Serializable
{
    private String keyword;
    private int userId;

    public SearchTweetsRequest (String keyword, int userId)
    {
        this.keyword = keyword;
        this.userId = userId;
    }

    public String getKeyword() { return keyword; }
    public int getUserId() {return userId; }
}
