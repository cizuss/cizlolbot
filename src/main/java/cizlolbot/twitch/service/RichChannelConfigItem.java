package cizlolbot.twitch.service;

import cizlolbot.twitch.model.ChannelConfigItem;
import cizlolbot.twitch.model.CommandResponse;

public class RichChannelConfigItem {
    private ChannelConfigItem channelConfigItem;
    private CommandResponse commandResponse;

    public RichChannelConfigItem(ChannelConfigItem channelConfigItem, CommandResponse commandResponse) {
        this.channelConfigItem = channelConfigItem;
        this.commandResponse = commandResponse;
    }

    public ChannelConfigItem getChannelConfigItem() {
        return channelConfigItem;
    }

    public CommandResponse getCommandResponse() {
        return commandResponse;
    }

}
