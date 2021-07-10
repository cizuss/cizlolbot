package cizlolbot.twitch.handlers;

import cizlolbot.twitch.TwitchService;
import cizlolbot.twitch.irc.IrcPrivateMessage;
import cizlolbot.twitch.irc.IrcUtils;
import cizlolbot.twitch.utils.StringUtils;

import java.util.function.Consumer;

/**
 * HandlerService implementation that simply writes "hi" or "hello" in the chat when somebody types "hi" or "hello";
 * It's rate limited in case there are a lot of greetings messages in a short period of time
 */
public class GreetingsHandlerService implements HandlerService {
    private TwitchService twitchService;

    private long window;
    private int nrMessages = 0;
    private int limitPerMinute = 3;

    public GreetingsHandlerService(TwitchService twitchService) {
        this.twitchService = twitchService;
    }

    @Override
    public Consumer<String> getHandler(String channel) {
        return line -> {
            if (IrcUtils.isPrivMsg(line)) {
                IrcPrivateMessage message = new IrcPrivateMessage(line);
                if (StringUtils.containsWord(message.getBody(), "hi")) {
                    throttle(() -> twitchService.sendMessage("hello :)", channel));
                } else if (StringUtils.containsWord(message.getBody(), "hello")) {
                    throttle(() -> twitchService.sendMessage("hi :)", channel));
                }
            }
        };
    }

    private void throttle(Runnable runnable) {
        long now = System.currentTimeMillis() / 60000;
        if (now > window) {
            window = now;
            nrMessages = 0;
        }
        if (nrMessages < limitPerMinute) {
            runnable.run();
            nrMessages++;
        }
    }
}
