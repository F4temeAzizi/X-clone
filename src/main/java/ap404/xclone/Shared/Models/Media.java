package ap404.xclone.Shared.Models;

import java.io.Serializable;

public class Media implements Serializable
{
    private int id;
    private int tweetId;
    private String mediaUrl;
    private String mediaType;
    private int mediaOrder;

    public Media(int id, int tweetId, String mediaUrl, String mediaType, int mediaOrder)
    {
        this.id = id;
        this.tweetId = tweetId;
        this.mediaUrl = mediaUrl;
        this.mediaType = mediaType;
        this.mediaOrder = mediaOrder;
    }

    public Media(String mediaUrl, String mediaType, int mediaOrder)
    {
        this.mediaUrl = mediaUrl;
        this.mediaType = mediaType;
        this.mediaOrder = mediaOrder;
    }

    public int getId() { return id; }
    public int getTweetId() { return tweetId; }
    public String getMediaUrl() { return mediaUrl; }
    public String getMediaType() { return mediaType; }
    public int getMediaOrder() { return mediaOrder; }
}
