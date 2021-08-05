package cizlolbot.twitch.api_response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GetModeratorsResponse {
    @JsonProperty("data")
    private List<TwitchUser> data;

    @JsonProperty("pagination")
    private TwitchPagination pagination;

    public List<TwitchUser> getData() {
        return data;
    }

    public GetModeratorsResponse setData(List<TwitchUser> data) {
        this.data = data;
        return this;
    }

    public TwitchPagination getPagination() {
        return pagination;
    }

    public GetModeratorsResponse setPagination(TwitchPagination pagination) {
        this.pagination = pagination;
        return this;
    }
}
