package cizlolbot.twitch.irc;

public class IrcUtils {
    public static boolean isPrivMsg(String line) {
        return line != null && line.contains("PRIVMSG");
    }
}
