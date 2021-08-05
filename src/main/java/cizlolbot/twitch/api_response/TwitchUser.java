package cizlolbot.twitch.api_response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TwitchUser {

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("user_login")
    private String userLogin;

    @JsonProperty("user_name")
    private String userName;

    public String getUserId() {
        return userId;
    }

    public TwitchUser setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public TwitchUser setUserLogin(String userLogin) {
        this.userLogin = userLogin;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public TwitchUser setUserName(String userName) {
        this.userName = userName;
        return this;
    }
}
