package cizlolbot.twitch.dao;

import cizlolbot.twitch.model.ChannelConfigItem;
import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class InMemoryChannelConfigItemDao implements ChannelConfigItemDao {
    private Map<String, List<ChannelConfigItem>> db = new HashMap<>();

    @Override
    public List<ChannelConfigItem> getByChannelName(String channelName) {
        return db.get(channelName);
    }

    @Override
    public void insertOrUpdate(ChannelConfigItem configItem) {
        if (!db.containsKey(configItem.getChannelName())) {
            db.put(configItem.getChannelName(), new ArrayList<>());
        }
        List<ChannelConfigItem> items = db.get(configItem.getChannelName());
        items.stream().filter(its -> its.getCommand().equals(configItem.getCommand()))
                .findFirst().ifPresent(items::remove);
        items.add(configItem);
    }

}
