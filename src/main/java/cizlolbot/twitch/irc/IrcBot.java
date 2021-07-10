package cizlolbot.twitch.irc;

import cizlolbot.twitch.TwitchService;
import cizlolbot.twitch.handlers.HandlerService;

public class IrcBot {
    private TwitchService twitchService;
    private HandlerService handlerService;

    public IrcBot(HandlerService handlerService) {
        this.handlerService = handlerService;

        twitchService = TwitchService.getInstance();
        twitchService.connectToIrcChat();
    }

    public void run(String channelName) {
        twitchService.connectToChannel(channelName);

        while(true) {
            String line = twitchService.readLine();
            System.out.println("Read " + line);

            handlerService.getHandler(channelName).accept(line);
        }
    }
}
