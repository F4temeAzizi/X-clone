package ap404.xclone.Shared.DTO.request;

import ap404.xclone.Shared.DTO.enums.RequestType;

import java.io.Serializable;

public class Request implements Serializable {

    private final RequestType type;
    private final Object body;

    public Request(RequestType type, Object body) {
        this.type = type;
        this.body = body;
    }

    public RequestType getType() { return type;}
    public Object getBody() { return body; }
}
