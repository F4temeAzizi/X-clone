package ap404.xclone.Shared.DTO.enums;

import java.io.Serializable;

public enum ResponseType implements Serializable {
    LOGIN_SUCCESS,
    LOGIN_FAILED,
    SIGNUP_SUCCESS,
    SIGNUP_FAILED,
    UPDATE_PROFILE_SUCCESS,
    UPDATE_PROFILE_FAILED,
    CREATE_TWEET_SUCCESS,
    CREATE_TWEET_FAILED,
    GET_TWEETS_SUCCESS,
    GET_TWEETS_FAILED,
    LIKE_SUCCESS,
    LIKE_FAILED,
    UNLIKE_SUCCESS,
    UNLIKE_FAILED
}
