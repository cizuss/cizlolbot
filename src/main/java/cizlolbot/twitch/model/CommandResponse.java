package cizlolbot.twitch.model;

public class CommandResponse {
    private String id;
    private CommandReponseType type;
    private String staticResponse;

    public String getId() {
        return id;
    }

    public CommandResponse setId(String id) {
        this.id = id;
        return this;
    }

    public CommandReponseType getType() {
        return type;
    }

    public CommandResponse setType(CommandReponseType type) {
        this.type = type;
        return this;
    }

    public String getStaticResponse() {
        return staticResponse;
    }

    public CommandResponse setStaticResponse(String staticResponse) {
        this.staticResponse = staticResponse;
        return this;
    }
}
