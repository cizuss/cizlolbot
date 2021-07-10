package cizlolbot.twitch.irc;

import cizlolbot.twitch.TwitchService;
import cizlolbot.twitch.handlers.HandlerService;

public class IrcBot {
    private TwitchService twitchService;
    private HandlerService handlerService;

    public IrcBot(TwitchService twitchService, HandlerService handlerService) {
        this.twitchService = twitchService;
        this.handlerService = handlerService;

        twitchService.connectToIrcChat();
    }

    public void run(String channelName) throws InterruptedException {
        twitchService.connectToChannel(channelName);

        while(true) {
            String line = twitchService.readLine();
            Thread.sleep(50);
            System.out.println("Read " + line);

            handlerService.getHandler(channelName).accept(line);
        }
    }
}
