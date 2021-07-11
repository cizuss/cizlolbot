package cizlolbot.twitch.irc;

import cizlolbot.twitch.service.TwitchService;
import cizlolbot.twitch.handlers.HandlerService;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class IrcBot {
    private TwitchService twitchService;
    private HandlerService handlerService;

    @Inject
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
