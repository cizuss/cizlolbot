package cizlolbot.twitch.handlers;

import cizlolbot.twitch.irc.IrcPrivateMessage;
import cizlolbot.twitch.irc.IrcUtils;
import cizlolbot.twitch.model.CommandReponseType;
import cizlolbot.twitch.model.CommandResponse;
import cizlolbot.twitch.model.CommandTriggerType;
import cizlolbot.twitch.ratelimit.RateLimiter;
import cizlolbot.twitch.service.ChannelConfigService;
import cizlolbot.twitch.service.RichChannelConfigItem;
import cizlolbot.twitch.service.TwitchService;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.Map;
import java.util.function.Consumer;

@Singleton
public class ChannelConfigBasedHandlerService implements HandlerService {
    private ChannelConfigService channelConfigService;
    private TwitchService twitchService;
    private RateLimiter rateLimiter;

    @Inject
    public ChannelConfigBasedHandlerService(ChannelConfigService channelConfigService, TwitchService twitchService,
                                            RateLimiter rateLimiter) {
        this.channelConfigService = channelConfigService;
        this.twitchService = twitchService;
        this.rateLimiter = rateLimiter;
    }

    @Override
    public Consumer<String> getHandler(String channel) {
        return line -> {
            if (IrcUtils.isPrivMsg(line)) {
                handlePrivMsg(line, channel);
            }
        };
    }

    private void handlePrivMsg(String line, String channel) {
        Map<String, RichChannelConfigItem> configGroupedByCommand = channelConfigService.getConfigGroupedByCommand(channel);
        IrcPrivateMessage message = new IrcPrivateMessage(line);

        String[] words = message.getBody().split(" ");
        for (int i=0; i<words.length; i++) {
            String word = words[i];
            RichChannelConfigItem configItem = configGroupedByCommand.get(word);
            if (configItem != null) {
                CommandTriggerType triggerType = configItem.getChannelConfigItem().getCommandTriggerType();
                if (CommandTriggerType.STARTS_WITH == triggerType && i == 0) {
                    handleCommandResponse(configItem.getCommandResponse(), channel, message);
                } else if (CommandTriggerType.EQUALS == triggerType && words.length == 1) {
                    handleCommandResponse(configItem.getCommandResponse(), channel, message);
                } else if (CommandTriggerType.CONTAINS == triggerType) {
                    handleCommandResponse(configItem.getCommandResponse(), channel, message);
                }
            }
        }
    }

    private void handleCommandResponse(CommandResponse commandResponse, String channel,
                                       IrcPrivateMessage message) {
        if (commandResponse.getType().equals(CommandReponseType.STATIC_REPLY)) {
            rateLimiter.throttle(commandResponse.getId(), () -> twitchService.sendMessage(commandResponse.getStaticResponse(), channel));
        } else if (commandResponse.getType().equals(CommandReponseType.STATIC_REPLY_TO_SENDER)) {
            String twitchMessage = String.format("@%s %s", message.getFrom(), commandResponse.getStaticResponse());
            rateLimiter.throttle(commandResponse.getId(), () -> twitchService.sendMessage(twitchMessage, channel));
        }
    }
}
