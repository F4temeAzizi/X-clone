package ap404.xclone.Shared.DTO.request;

import java.io.Serializable;

public class GetUserByIdRequest implements Serializable {
    private int userId;

    public GetUserByIdRequest(int userId) {
        this.userId = userId;
    }

    public int getUserId() { return userId; }
}
