package cizlolbot.twitch.service;

import cizlolbot.twitch.dao.ChannelConfigItemDao;
import cizlolbot.twitch.dao.CommandResponseDao;
import cizlolbot.twitch.model.ChannelConfigItem;
import cizlolbot.twitch.model.CommandReponseType;
import cizlolbot.twitch.model.CommandResponse;
import cizlolbot.twitch.model.CommandTriggerType;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ChannelConfigService {
    private ChannelConfigItemDao channelConfigItemDao;
    private CommandResponseDao commandResponseDao;

    private static ChannelConfigService instance;

    private Map<String, Map<String, RichChannelConfigItem>> cachedConfig = new ConcurrentHashMap<>();

    private ChannelConfigService(ChannelConfigItemDao channelConfigItemDao,
                                 CommandResponseDao  commandResponseDao) {
        this.channelConfigItemDao = channelConfigItemDao;
        this.commandResponseDao = commandResponseDao;
    }

    public static ChannelConfigService getInstance(ChannelConfigItemDao channelConfigItemDao,
                                                   CommandResponseDao commandResponseDao) {
        if (instance == null) {
            instance = new ChannelConfigService(channelConfigItemDao, commandResponseDao);
        }
        return instance;
    }

    public void addStaticReply(String channel, String command, String staticReply, CommandTriggerType triggerType) {
        CommandResponse commandResponse = new CommandResponse().setId(UUID.randomUUID().toString())
                .setType(CommandReponseType.STATIC_REPLY).setStaticResponse(staticReply);

        ChannelConfigItem configItem = new ChannelConfigItem().setChannelName(channel).setCommand(command)
                .setCommandTriggerType(triggerType).setCommandResponseId(commandResponse.getId());

        commandResponseDao.insert(commandResponse);
        channelConfigItemDao.insert(configItem);

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
