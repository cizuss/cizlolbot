package cizlolbot.twitch.dao;

import cizlolbot.twitch.model.ChannelConfigItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryChannelConfigItemDao implements ChannelConfigItemDao {
    private Map<String, List<ChannelConfigItem>> db = new HashMap<>();
    private static InMemoryChannelConfigItemDao instance;

    private InMemoryChannelConfigItemDao() {

    }

    public static InMemoryChannelConfigItemDao getInstance() {
        if (instance == null) {
            instance = new InMemoryChannelConfigItemDao();
        }
        return instance;
    }

    @Override
    public List<ChannelConfigItem> getByChannelName(String channelName) {
        return db.get(channelName);
    }

    @Override
    public void insert(ChannelConfigItem configItem) {
        if (!db.containsKey(configItem.getChannelName())) {
            db.put(configItem.getChannelName(), new ArrayList<>());
        }
        List<ChannelConfigItem> items = db.get(configItem.getChannelName());
        items.add(configItem);
    }

}
