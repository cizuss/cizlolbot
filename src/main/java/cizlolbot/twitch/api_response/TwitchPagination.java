package cizlolbot.twitch.api_response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TwitchPagination {
    @JsonProperty("cursor")
    private String cursor;

    public String getCursor() {
        return cursor;
    }

    public TwitchPagination setCursor(String cursor) {
        this.cursor = cursor;
        return this;
    }
}
