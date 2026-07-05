package ap404.xclone.Shared;

import java.io.Serializable;

public class Response implements Serializable {

    private ResponseType type;
    private Object body;

    public Response(ResponseType type) { this.type = type; }

    public Response(ResponseType type, Object body) {
        this.type = type;
        this.body = body;
    }

    public ResponseType getType() { return type; }
    public Object getBody() { return body; }
}
