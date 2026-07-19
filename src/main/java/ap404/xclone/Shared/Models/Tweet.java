package ap404.xclone.Shared.Models;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class Tweet implements Serializable {

    private int id;
    private String content;
    private Timestamp createdAt;
    private Integer retweetOfId;
    private Integer replyToId;

    private int userId;
    private String name;
    private String username;

    private int likeCount;
    private boolean isLikedByUser;

    private int retweetCount;
    private boolean isRetweet;
    private boolean isRetweetedByUser;
    private Tweet originalTweet;

    private String avatarImageUrl;

    private boolean isBookmarkedByUser;

    private List<Media> media;

    public Tweet(int id, int userId, String content, Timestamp createdAt,
                 Integer retweetOfId, Integer replyToId,
                 String name, String username, int likeCount, boolean isLikedByUser,
                 String avatarImageUrl, boolean isBookmarkedByUser, int retweetCount, boolean isRetweet,
                 boolean isRetweetedByUser
    ) {
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
        this.isBookmarkedByUser = isBookmarkedByUser;
        this.retweetCount = retweetCount;
        this.isRetweet = isRetweet;
        this.isRetweetedByUser = isRetweetedByUser;
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

    public boolean isBookmarkedByUser() { return isBookmarkedByUser; }
    public void setBookmarkedByUser(boolean isBookmarkedByUser) { this.isBookmarkedByUser = isBookmarkedByUser; }

    public List<Media> getMedia() { return media; }
    public void setMedia(List<Media> media) { this.media = media; }

    public int getRetweetCount() { return retweetCount; }
    public void setRetweetCount(int retweetCount) { this.retweetCount = retweetCount; }

    public boolean isRetweet() { return isRetweet; }
    public void setRetweet(boolean isRetweet) { this.isRetweet = isRetweet; }

    public boolean isRetweetedByUser() {return isRetweetedByUser; }
    public void setRetweetedByUser(boolean isRetweetedByUser) { this.isRetweetedByUser = isRetweetedByUser; }

    public Tweet getOriginalTweet() { return originalTweet; }
    public void setOriginalTweet(Tweet originalTweet) { this.originalTweet = originalTweet; }
}