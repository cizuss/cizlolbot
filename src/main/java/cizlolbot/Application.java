package cizlolbot;

import cizlolbot.twitch.TwitchService;
import cizlolbot.twitch.handlers.GreetingsHandlerService;
import cizlolbot.twitch.irc.IrcBot;

public class Application {
    public static void main(String[] args) throws InterruptedException {
        IrcBot ircBot = new IrcBot(TwitchService.getInstance(),
                new GreetingsHandlerService(TwitchService.getInstance()));

        ircBot.run("lck");
    }
}
