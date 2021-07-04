package cizlolbot;

import cizlolbot.twitch.irc.IrcBot;

public class Application {
    public static void main(String[] args) {
        IrcBot ircBot = new IrcBot("lcs");
        ircBot.connect();

        while(true) {
            ircBot.readLine();
        }
    }
}
