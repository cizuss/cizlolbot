package cizlolbot;

import cizlolbot.twitch.handlers.GreetingsHandlerService;
import cizlolbot.twitch.irc.IrcBot;

public class Application {
    public static void main(String[] args) {
        IrcBot ircBot = new IrcBot(new GreetingsHandlerService());

        ircBot.run("lck");
    }
}
