package ap404.xclone.Shared;

import java.io.Serializable;

public enum ResponseType implements Serializable {
    LOGIN_SUCCESS,
    LOGIN_FAILED,
    SIGNUP_SUCCESS,
    SIGNUP_FAILED,
    UPDATE_PROFILE_SUCCESS,
    UPDATE_PROFILE_FAILED
}
