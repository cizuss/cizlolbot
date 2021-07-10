package cizlolbot.twitch.irc;

public class IrcPrivateMessage {
    private String from;
    private String to;
    private String body;

    public IrcPrivateMessage(String rawLine) {
        from = rawLine.substring(1, rawLine.indexOf("!"));

        int atPos = rawLine.indexOf(":@");
        if (atPos != -1) {
            String remainingLine = rawLine.substring(atPos);
            to = remainingLine.split(" \t")[0].substring(2);
        }

        String content = rawLine.substring(rawLine.indexOf("PRIVMSG") + "PRIVMSG".length());
        body = content.split(":")[1];
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getBody() {
        return body;
    }
}
