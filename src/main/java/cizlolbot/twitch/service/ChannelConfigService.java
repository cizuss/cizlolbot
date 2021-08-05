package cizlolbot.twitch.service;

import cizlolbot.twitch.dao.ChannelConfigItemDao;
import cizlolbot.twitch.dao.CommandResponseDao;
import cizlolbot.twitch.model.ChannelConfigItem;
import cizlolbot.twitch.model.CommandReponseType;
import cizlolbot.twitch.model.CommandResponse;
import cizlolbot.twitch.model.CommandTriggerType;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Singleton
public class ChannelConfigService {
    private ChannelConfigItemDao channelConfigItemDao;
    private CommandResponseDao commandResponseDao;

    private Map<String, Map<String, RichChannelConfigItem>> cachedConfig = new ConcurrentHashMap<>();

    @Inject
    public ChannelConfigService(ChannelConfigItemDao channelConfigItemDao,
                                 CommandResponseDao  commandResponseDao) {
        this.channelConfigItemDao = channelConfigItemDao;
        this.commandResponseDao = commandResponseDao;
    }


    public void addStaticReply(String channel, String command, String staticReply, CommandTriggerType triggerType) {
        CommandResponse commandResponse = new CommandResponse().setId(UUID.randomUUID().toString())
                .setType(CommandReponseType.STATIC_REPLY).setStaticResponse(staticReply);

        addConfig(channel, command, triggerType, commandResponse);
    }

    public void addSetCommand(String channel, String command) {
        CommandResponse commandResponse = new CommandResponse().setId(UUID.randomUUID().toString())
                .setType(CommandReponseType.SET_COMMAND);

        addConfig(channel, command, CommandTriggerType.STARTS_WITH, commandResponse);
    }

    public void addStaticReplyToSender(String channel, String command, String staticReply, CommandTriggerType triggerType) {
        CommandResponse commandResponse = new CommandResponse().setId(UUID.randomUUID().toString())
                .setType(CommandReponseType.STATIC_REPLY_TO_SENDER).setStaticResponse(staticReply);

        addConfig(channel, command, triggerType, commandResponse);
    }

    public void addSetLolRegion(String channel, String command) {
        CommandResponse commandResponse = new CommandResponse().setId(UUID.randomUUID().toString())
                .setType(CommandReponseType.SET_REGION).setCommand(command);

        addConfig(channel, command, CommandTriggerType.STARTS_WITH, commandResponse);
    }

    public void addSetLolName(String channel, String command) {
        CommandResponse commandResponse = new CommandResponse().setId(UUID.randomUUID().toString())
                .setType(CommandReponseType.SET_SUMMONER_NAME).setCommand(command);

        addConfig(channel, command, CommandTriggerType.STARTS_WITH, commandResponse);
    }

    public void addOpGGCommand(String channel, String command) {
        CommandResponse commandResponse = new CommandResponse().setId(UUID.randomUUID().toString())
                .setType(CommandReponseType.OPGG_LINK).setCommand(command);

        addConfig(channel, command, CommandTriggerType.EQUALS, commandResponse);
    }

    public void addRankCommand(String channel, String command) {
        CommandResponse commandResponse = new CommandResponse().setId(UUID.randomUUID().toString())
                .setType(CommandReponseType.RANK).setCommand(command);

        addConfig(channel, command, CommandTriggerType.EQUALS, commandResponse);
    }

    private void addConfig(String channel, String command, CommandTriggerType triggerType, CommandResponse commandResponse) {
        ChannelConfigItem configItem = new ChannelConfigItem().setChannelName(channel).setCommand(command)
                .setCommandTriggerType(triggerType).setCommandResponseId(commandResponse.getId());

        commandResponseDao.insert(commandResponse);
        channelConfigItemDao.insertOrUpdate(configItem);

        cachedConfig.remove(channel);
    }

    public Map<String, RichChannelConfigItem> getConfigGroupedByCommand(String channel) {
        if (cachedConfig.containsKey(channel)) {
            return cachedConfig.get(channel);
        }

        List<ChannelConfigItem> configItems = channelConfigItemDao.getByChannelName(channel);
        Map<String, CommandResponse> commandResponseMap =
                commandResponseDao.findByIds(configItems.stream()
                        .map(ChannelConfigItem::getCommandResponseId).collect(Collectors.toSet()));

        Map<String, RichChannelConfigItem> config = configItems.stream()
                .collect(Collectors.toMap(ChannelConfigItem::getCommand,
                        configItem -> new RichChannelConfigItem(configItem, commandResponseMap.get(configItem.getCommandResponseId()))));
        cachedConfig.put(channel, config);
        return config;
    }
}
