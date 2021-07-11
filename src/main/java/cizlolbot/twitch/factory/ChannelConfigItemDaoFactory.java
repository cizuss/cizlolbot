package cizlolbot.twitch.factory;

import cizlolbot.twitch.dao.ChannelConfigItemDao;
import cizlolbot.twitch.dao.InMemoryChannelConfigItemDao;

public class ChannelConfigItemDaoFactory {
    public static ChannelConfigItemDao createDao() {
        return InMemoryChannelConfigItemDao.getInstance();
    }
}
