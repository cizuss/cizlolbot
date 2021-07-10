package cizlolbot.twitch.handlers;

import java.util.function.Consumer;


public interface HandlerService {
    /**
     * Provides a handler to a IRC line in a channel
     * @param channel (the channel name)
     * @return a Consumer[String] that consumes a IRC chat line and does something with it (for example, responds back in the chat)
     */
    Consumer<String> getHandler(String channel);
}
