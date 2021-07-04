package cizlolbot.twitch.oauth2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OauthResponse {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private long expiresIn;

    @JsonProperty
    private List<String> scope;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonIgnore
    private long expiresAt;

    public String getAccessToken() {
        return accessToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public List<String> getScope() {
        return scope;
    }

    public String getTokenType() {
        return tokenType;
    }

    public OauthResponse setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public OauthResponse setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

    public OauthResponse setScope(List<String> scope) {
        this.scope = scope;
        return this;
    }

    public OauthResponse setTokenType(String tokenType) {
        this.tokenType = tokenType;
        return this;
    }

    public long getExpiresAt() {
        return expiresAt;
    }

    public OauthResponse setExpiresAt(long expiresAt) {
        this.expiresAt = expiresAt;
        return this;
    }
}
