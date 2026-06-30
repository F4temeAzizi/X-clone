package ap404.xclone.Shared;

import java.io.Serializable;

public class Response implements Serializable {

    private RequestType type;

    public Response(RequestType type) {
        this.type = type;
    }

    private RequestType getType() { return type; }
}
