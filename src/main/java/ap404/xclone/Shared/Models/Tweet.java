package ap404.xclone.Shared.Models;

import java.io.Serializable;
import java.sql.Timestamp;

public class Tweet implements Serializable {

    private int id;
    private int userId;
    private String content;
    private Timestamp createdAt;

    private boolean isRetweet;
    private Integer retweetOfId;
    private Integer replyToId;

    private String name;
    private String username;
    private int likeCount;
    private boolean isLikedByUser;

    private String avatarImageUrl;

    public Tweet(int id, int userId, String content, Timestamp createdAt,
                 boolean isRetweet, Integer retweetOfId, Integer replyToId,
                 String name, String username, int likeCount, boolean isLikedByUser, String avatarImageUrl) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.createdAt = createdAt;
        this.isRetweet = isRetweet;
        this.retweetOfId = retweetOfId;
        this.replyToId = replyToId;
        this.name = name;
        this.username = username;
        this.likeCount = likeCount;
        this.isLikedByUser = isLikedByUser;
        this.avatarImageUrl = avatarImageUrl;
    }

    public String getAvatarImageUrl() { return avatarImageUrl; }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }


    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public boolean isRetweet() {
        return isRetweet;
    }

    public Integer getRetweetOfId() {
        return retweetOfId;
    }

    public Integer getReplyToId() {
        return replyToId;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public int getLikeCount() { return likeCount; }
    public void setLikeCount(int likeCount) { this.likeCount = likeCount; }

    public boolean isLikedByUser() { return isLikedByUser; }
    public void setLikedByUser(boolean isLikedByUser) { this.isLikedByUser = isLikedByUser; }
}