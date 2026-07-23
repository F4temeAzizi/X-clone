package ap404.xclone.Shared.DTO.request;

import java.io.Serializable;

public class ShowHashtagRequest implements Serializable
{
    String hashtag;
    int userId;

    public ShowHashtagRequest(String hashtag, int userId)
    {
        this.hashtag = hashtag;
        this.userId = userId;
    }

    public String getHashtag() { return hashtag; }
    public int getUserId() { return userId; }
}
