package ap404.xclone.Shared.DTO.request;

import java.io.Serializable;

public class CreateTweetRequest implements Serializable {
    private int userId;
    private String content;

    public CreateTweetRequest(int userId, String content) {
        this.userId = userId;
        this.content = content;
    }

    public int getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }
}