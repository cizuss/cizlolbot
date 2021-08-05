package cizlolbot.twitch.riot;

public class ConstructOpGgException extends Exception {
    private String twitchMessage;

    public ConstructOpGgException(String twitchMessage) {
        this.twitchMessage = twitchMessage;
    }

    public String getTwitchMessage() {
        return twitchMessage;
    }
}
