package ap404.xclone.Shared.DTO.enums;

import java.io.Serializable;

public enum RequestType implements Serializable {
    LOGIN,
    SIGNUP,
    UPDATE_PROFILE,
    CREATE_TWEET,
    GET_ALL_TWEETS,
    LIKE,
    UNLIKE,
    GET_USER_BY_ID,
    GET_TWEETS_BY_USER,
    DELETE_TWEET,
    EDIT_TWEET,
    GET_LIKED_TWEETS,
    BOOKMARK,
    UNBOOKMARK,
    GET_BOOKMARKED_TWEETS,
    RETWEET,
    UNRETWEET,
    FOLLOW,
    UNFOLLOW,
    CHECK_FOLLOW,
    REPLY,
    GET_TWEET_REPLIES,
    GET_USER_REPLIES
}
