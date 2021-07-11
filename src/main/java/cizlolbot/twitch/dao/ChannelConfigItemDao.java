package cizlolbot.twitch.dao;

import cizlolbot.twitch.model.ChannelConfigItem;

import java.util.List;

public interface ChannelConfigItemDao {
    List<ChannelConfigItem> getByChannelName(String channelName);
    void insert(ChannelConfigItem configItem);
}
