package ap404.xclone.Shared;

import java.io.Serializable;

public class Response implements Serializable {

    private ResponseType type;

    public Response(ResponseType type) {
        this.type = type;
    }

    private ResponseType getType() { return type; }
}
