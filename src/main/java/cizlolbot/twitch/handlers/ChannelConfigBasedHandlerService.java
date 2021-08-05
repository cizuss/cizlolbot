package cizlolbot.twitch.handlers;

import cizlolbot.twitch.irc.IrcPrivateMessage;
import cizlolbot.twitch.irc.IrcUtils;
import cizlolbot.twitch.model.CommandReponseType;
import cizlolbot.twitch.model.CommandResponse;
import cizlolbot.twitch.model.CommandTriggerType;
import cizlolbot.twitch.ratelimit.RateLimiter;
import cizlolbot.twitch.riot.ConstructOpGgException;
import cizlolbot.twitch.riot.LolService;
import cizlolbot.twitch.riot.Region;
import cizlolbot.twitch.service.ChannelConfigService;
import cizlolbot.twitch.service.RichChannelConfigItem;
import cizlolbot.twitch.service.TwitchService;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;

@Singleton
public class ChannelConfigBasedHandlerService implements HandlerService {
    private ChannelConfigService channelConfigService;
    private TwitchService twitchService;
    private RateLimiter rateLimiter;
    private LolService lolService;

    @Inject
    public ChannelConfigBasedHandlerService(ChannelConfigService channelConfigService, TwitchService twitchService,
                                            RateLimiter rateLimiter, LolService lolService) {
        this.channelConfigService = channelConfigService;
        this.twitchService = twitchService;
        this.rateLimiter = rateLimiter;
        this.lolService = lolService;
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
        for (int i = 0; i < words.length; i++) {
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
        } else if (commandResponse.getType().equals(CommandReponseType.SET_SUMMONER_NAME)) {
            ensureUserCanExecuteCommand(message.getFrom(), channel, () -> handleSetSummonerName(channel, message, commandResponse.getCommand()));
        } else if (commandResponse.getType().equals(CommandReponseType.SET_REGION)) {
            ensureUserCanExecuteCommand(message.getFrom(), channel, () -> handleSetRegion(channel, message, commandResponse.getCommand()));
        } else if (commandResponse.getType().equals(CommandReponseType.OPGG_LINK)) {
            handleOpGg(channel, message);
        } else if (commandResponse.getType().equals(CommandReponseType.RANK)) {
            handleRank(channel, message);
        } else if (commandResponse.getType().equals(CommandReponseType.SET_COMMAND)) {
            ensureUserCanExecuteCommand(message.getFrom(), channel, () -> handleSetNewCommand(channel, message));
        }
    }

    private void ensureUserCanExecuteCommand(String user, String channel, Runnable runnable) {
        boolean isBroadcaster = user.equals(channel);

        if (isBroadcaster || twitchService.isModerator(user, channel)) {
            runnable.run();
        }
    }

    private void handleSetNewCommand(String channel, IrcPrivateMessage message) {
        String[] words = message.getBody().split(" ");
        if (words.length < 3) {
            twitchService.sendMessage("Invalid format of set new command", channel);
            return;
        }
        String newCommand = words[1];
        StringBuilder staticResponseSB = new StringBuilder();
        for (int i = 2; i < words.length; i++) {
            staticResponseSB.append(words[i]);
            staticResponseSB.append(" ");
        }
        String staticResponse = staticResponseSB.toString();

        channelConfigService.addStaticReply(channel, newCommand, staticResponse, CommandTriggerType.EQUALS);
        twitchService.sendMessage(String.format("Successfully set new command %s to \"%s\"", newCommand, staticResponse), channel);
    }

    private void handleRank(String channel, IrcPrivateMessage message) {
        String twitchMessage;
        try {
            String rank = lolService.getRank(channel);
            twitchMessage = String.format("@%s %s", message.getFrom(), rank);
        } catch (ConstructOpGgException e) {
            twitchMessage = e.getTwitchMessage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        twitchService.sendMessage(twitchMessage, channel);
    }

    private void handleOpGg(String channel, IrcPrivateMessage message) {
        String twitchMessage;
        try {
            String opGgLink = lolService.constructOpGGLink(channel);
            twitchMessage = String.format("@%s %s", message.getFrom(), opGgLink);
        } catch (ConstructOpGgException e) {
            twitchMessage = e.getTwitchMessage();
        }

        twitchService.sendMessage(twitchMessage, channel);
    }

    private void handleSetRegion(String channel, IrcPrivateMessage message, String command) {
        String[] words = message.getBody().split(" ");
        if (words.length != 2) {
            String twitchMessage = String.format("Invalid format of command set region; Correct format is \"%s <region>\"", command);
            twitchService.sendMessage(twitchMessage, channel);
            return;
        }
        String regionString = words[1];
        Region region;
        try {
            region = Region.valueOf(regionString);
        } catch (Exception e) {
            String twitchMessage = String.format("Region needs to be one of %s", Arrays.toString(Region.values()));
            twitchService.sendMessage(twitchMessage, channel);
            return;
        }

        lolService.setRegion(channel, region);
        String twitchMessage = String.format("Successfully set region to %s", regionString);
        twitchService.sendMessage(twitchMessage, channel);
    }

    private void handleSetSummonerName(String channel, IrcPrivateMessage message, String command) {
        String[] words = message.getBody().split(" ");
        if (words.length != 2) {
            String twitchMessage = String.format("Invalid format of command set summoner name; Correct format is \"%s <summoner_name>\"", command);
            twitchService.sendMessage(twitchMessage, channel);
            return;
        }
        String summonerName = words[1];
        lolService.setSummonerName(channel, summonerName);
        String twitchMessage = String.format("Successfully set summoner name to %s", summonerName);
        twitchService.sendMessage(twitchMessage, channel);
    }
}
